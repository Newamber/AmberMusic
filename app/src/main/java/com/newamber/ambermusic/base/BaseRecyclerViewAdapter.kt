/*
 * MIT License
 *
 * Copyright (c) 2018 Newamber(Liu Enbei)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.newamber.ambermusic.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newamber.ambermusic.mvp.model.AmberRepository
import com.newamber.ambermusic.util.OnItemClickListener
import com.newamber.ambermusic.util.OnItemLongClickListener
import com.newamber.ambermusic.util.OnSubItemClickListener
import com.newamber.ambermusic.util.OnSubItemLongClickListener

/**
 * Description: Base Adapter for RecyclerView.
 *
 * Usage:
 *
 * ```
 *      // assume FooAdapter is a sub class of BaseRecyclerViewAdapter
 *      val someFooList = mutableListOf(...)
 *      val adapter = FooAdapter(R.layout.recyclerview_item, FooDiffRuleCallBack())
 *
 *      // refreshBy() is not necessary if you want to pass list object later
 *      adapter.refreshBy(someFooList)
 *      recyclerView.setAdapter(adapter)
 * ```
 *
 * Created by Newamber on 2018/5/8.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class BaseRecyclerViewAdapter<T>(
    entityList: MutableList<T>,
    @LayoutRes private val layoutId: Int,
    diffRuleCallBack: BaseDiffRuleCallBack<T>
) : RecyclerView.Adapter<BaseViewHolder>() {

    protected val repository by lazy { AmberRepository() }

    private val diffHelper by lazy {
        AsyncListDiffer<T>(this, diffRuleCallBack)
    }

    val isEmpty = getEntityList().isEmpty()

    var headerView: View? = null
        set(value) {
            field = value
            notifyItemInserted(0)
        }

    var footerView: View? = null
        set(value) {
            field = value
            notifyItemInserted(itemCount - 1)
        }

    var emptyView: View? = null
        set(value) {
            field = value
            refreshBy(mutableListOf())
        }

    private var itemClickListener: OnItemClickListener<T>? = null
    private var itemLongClickListener: OnItemLongClickListener<T>? = null
    private var subClickListenerMap = hashMapOf<Int, OnSubItemClickListener<T>>()
    private var subLongClickListenerMap = hashMapOf<Int, OnSubItemLongClickListener<T>>()

    init {
        // if outer invoker passes no entityList, it will init with an empty list
        refreshBy(entityList)
    }

    /**
     * Extract concrete operations about different data binding. This is a
     * specific job of sub adapters according to the show of different item view.
     * You can bind text, color or drawable resource with view in it.
     *
     * @param holder the outer layer holder of sub items
     * @param entity the data source
     *
     * @author Newamber
     */
    abstract fun convertView(holder: BaseViewHolder, entity: T)

    // -----------------------------------API-------------------------------------------------------
    // set listeners
    fun setOnClickListener(listener: OnItemClickListener<T>) {
        itemClickListener = listener
    }

    fun setOnLongClickListener(listener: OnItemLongClickListener<T>) {
        itemLongClickListener = listener
    }

    fun addOnSubItemClickListener(@IdRes subViewId: Int, listener: OnSubItemClickListener<T>) {
        subClickListenerMap[subViewId] = listener
    }

    fun addOnSubLongItemClickListener(@IdRes subViewId: Int, listener: OnSubItemLongClickListener<T>) {
        subLongClickListenerMap[subViewId] = listener
    }

    /**
     * Refresh data by effective [AsyncListDiffer].
     *
     * @param newDataList new data list
     *
     * @author Newamber
     */
    fun refreshBy(newDataList: MutableList<T>) {
        diffHelper.submitList(newDataList)
    }

    // data list method
    fun getEntityList(): List<T> = diffHelper.currentList

    fun getEntity(position: Int): T = getEntityList()[position]

    fun getPosition(entity: T) = getEntityList().indexOf(entity)

    fun removeHeader() {
        if (headerView != null) {
            headerView = null
            notifyItemRemoved(0)
        }
    }

    fun removeFooter() {
        if (footerView != null) {
            footerView = null
            notifyItemRemoved(itemCount - 1)
        }
    }

    /**
     * TODO: Desc:
     *
     * @param key the
     * @param value the
     * @param holder the
     *
     * @author Newamber
     */
    protected open fun refreshLocalRule(key: String, value: Any, holder: BaseViewHolder) {}

    // base method
    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val context = parent.context
        return when (viewType) {
            ITEM_TYPE_HEADER -> BaseViewHolder(headerView, context)
            ITEM_TYPE_FOOTER -> BaseViewHolder(footerView, context)
            ITEM_TYPE_EMPTY -> BaseViewHolder(emptyView, context)
            else -> {
                val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
                BaseViewHolder(view, context)
            }
        }
    }

    final override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_HEADER, ITEM_TYPE_FOOTER, ITEM_TYPE_EMPTY -> return
        }
        dispatchOnItemClickListener(holder)
        dispatchOnSubItemClickListener(holder)
        convertView(holder, getEntityList()[getRealEntityPosition(position)])
    }

    final override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val diffBundle = payloads[0] as Bundle
            diffBundle.keySet().forEach {
                refreshLocalRule(it, diffBundle[it], holder)
            }
        }
    }

    final override fun getItemViewType(position: Int): Int {
        return when {
            headerView != null && position == 0 -> ITEM_TYPE_HEADER
            footerView != null && position == itemCount - 1 -> ITEM_TYPE_FOOTER
            emptyView != null && isEmpty -> ITEM_TYPE_EMPTY
            else -> ITEM_TYPE_NORMAL
        }
    }

    final override fun getItemCount(): Int {
        var itemCount = getEntityList().size
        if (emptyView != null && itemCount == 0) itemCount++
        if (headerView != null) itemCount++
        if (footerView != null) itemCount++

        return itemCount
    }

    companion object {
        // avoid using magic number
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_HEADER = 2
        private const val ITEM_TYPE_FOOTER = 3
        private const val ITEM_TYPE_EMPTY = 4
    }

    // onClick listener
//    @FunctionalInterface
//    interface OnItemClickListener<T> {
//        fun onItemClick(view: View, data: T, position: Int)
//    }
//
//    @FunctionalInterface
//    interface OnItemLongClickListener<T> {
//        fun onItemLongClick(view: View, data: T, position: Int)
//
//    }
//
//    @FunctionalInterface
//    interface OnSubItemClickListener<T> {
//        fun onSubItemClick(view: View, data: T, position: Int)
//
//    }
//
//    @FunctionalInterface
//    interface OnSubItemLongClickListener<T> {
//        fun onSubItemLongClick(view: View, data: T, position: Int)

//    }


    // -------------------------------private method------------------------------------------------
    // An auxiliary method to initialize (long) click listener.
    private fun dispatchOnItemClickListener(holder: BaseViewHolder) {
        itemClickListener?.apply {
            holder.itemView.setOnClickListener {
                val position = holder.layoutPosition
                this(it, getEntityList()[position], position)
            }
        }
        itemLongClickListener?.apply {
            holder.itemView.setOnLongClickListener {
                val position = holder.layoutPosition
                this(it, getEntityList()[position], position)
                true
            }
        }
    }

    // An auxiliary method to initialize (long) sub item click listener.
    private fun dispatchOnSubItemClickListener(holder: BaseViewHolder) {
        subClickListenerMap.forEach {
            holder.itemView.findViewById<View>(it.key).setOnClickListener { view ->
                val position = holder.layoutPosition
                it.value(view, getEntityList()[position], position)
            }
        }
        subLongClickListenerMap.forEach {
            holder.itemView.findViewById<View>(it.key).setOnClickListener { view ->
                val position = holder.layoutPosition
                it.value(view, getEntityList()[position], position)
            }
        }

    }

    private fun getRealEntityPosition(position: Int) =
        if (headerView != null) position - 1 else position

}
