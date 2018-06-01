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

import android.content.Context
import android.net.Uri
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.florent37.glidepalette.GlidePalette
import com.newamber.ambermusic.R

/**
 * Description: Base ViewHolder for [RecyclerView].
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/9.
 */
class BaseViewHolder(itemView: View?, private val context: Context) :
    RecyclerView.ViewHolder(itemView) {

    private val subViewArray: SparseArray<View> = SparseArray()

    fun setText(@IdRes viewId: Int, text: CharSequence) {
        val view: TextView = getSubView(viewId)
        view.text = text
    }

    fun setButtonText(@IdRes viewId: Int, text: CharSequence) {
        val view: Button = getSubView(viewId)
        view.text = text
    }

    fun setImage(@IdRes viewId: Int, @DrawableRes drawableId: Int) {
        //Glide.with(context).load(drawableId).preload()
        Glide.with(context).load(drawableId).into(getSubView(viewId))
    }

    fun setCardColor(@IdRes viewId: Int, @ColorInt colorInt: Int) {
        getSubView<CardView>(viewId).setCardBackgroundColor(colorInt)
    }

    fun setImage(@IdRes viewId: Int, uri: Uri?) {
        //Glide.with(context).load(uri).preload()
        Glide.with(context).load(uri).into(getSubView(viewId))
    }

    fun setThemeColor(@IdRes to: Int, @IdRes bg: Int, uri: Uri?) {
        //Glide.with(context).load(uri).preload()
        Glide.with(context)
            .load(uri)
            .listener(
                GlidePalette.with(uri.toString()).intoBackground(getSubView(bg))
            )
            .into(getSubView(to))
    }

    fun setThemeColor(@IdRes to: Int, @IdRes bg: Int, uri: String?) {
        //Glide.with(context).load(uri).preload()
        Glide.with(context)
            .load(uri)
            .listener(
                GlidePalette.with(uri).intoBackground(getSubView(bg))
            )
            .into(getSubView(to))
    }

    fun setImage(@IdRes viewId: Int, url: String) {
        //Glide.with(context).load(url).preload()
        Glide.with(context).load(url)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.bg_navigation)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            )
            .into(getSubView(viewId))
    }

    /**
     * Get item view according to IdRes.
     * The method can avoid redundant findViewById codes.
     *
     * @param viewId the id resource of view which we want to get
     * @param V certain view type depends on `viewId`
     * @return `view` which is converted to `T`
     *
     * @author Newamber
     */
    private fun <V : View> getSubView(@IdRes viewId: Int): V {
        var view = subViewArray.get(viewId)
        if (view == null) {
            view = itemView.findViewById<View>(viewId)
            subViewArray.put(viewId, view)
        }
        @Suppress("UNCHECKED_CAST")
        return view as V
    }

}