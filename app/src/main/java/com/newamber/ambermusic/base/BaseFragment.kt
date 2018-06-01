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

import android.net.Uri
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lsxiao.apollo.core.Apollo
import com.lsxiao.apollo.core.contract.ApolloBinder
import dagger.android.support.DaggerFragment

/**
 * Description: .
 * Created by Newamber on 2018/4/7.
 */
abstract class BaseFragment<V : BaseView, P : BasePresenter<V>> : DaggerFragment(),
    View.OnClickListener {

    protected abstract var presenter: P
    protected open var enableRxBus = false

    private var rootView: View? = null
    private var isVisibleToUser = false
    private var isViewCreated = false
    private var isDataInitialized = false
    private var rxBinder: ApolloBinder? = null

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        lazyInit()
    }

    // lifecycle method
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this as V)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false)
        }
        initView()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (enableRxBus) rxBinder = Apollo.bind(this)
        isViewCreated = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setMultiListener()
        lazyInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (enableRxBus) {
            rxBinder?.unbind()
            rxBinder = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        rootView = null
    }

    // sub class method
    @LayoutRes
    abstract fun getLayoutId(): Int

    protected open fun initView() {}

    protected open fun initData() {}

    protected open fun setMultiListener() {}

    protected open fun processClickEvent(@IdRes viewId: Int) {}

    protected open fun enableRxBus() = false

    protected fun setImage(view: ImageView, uri: Uri) {
        Glide.with(this).load(uri).into(view)
    }

    protected fun setOnClickListeners(vararg views: View) {
        views.forEach { it.setOnClickListener(this) }
    }

    protected fun setImage(view: ImageView, @DrawableRes resId: Int) {
        Glide.with(this).load(resId).into(view)
    }

    final override fun onClick(v: View) = processClickEvent(v.id)

    // -----------------------------------------private---------------------------------------------
    private fun lazyInit() {
        if (!isDataInitialized && isVisibleToUser && isViewCreated) {
            isDataInitialized = true
            initData()
        }
    }
}