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
import android.support.annotation.MenuRes
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.lsxiao.apollo.core.Apollo
import com.lsxiao.apollo.core.contract.ApolloBinder
import com.newamber.ambermusic.R
import com.newamber.ambermusic.util.ActivityController
import dagger.android.support.DaggerAppCompatActivity

/**
 * Description: MVP - BaseActivity binds the life circle of presenter
 * and offers some common methods to sub class.
 *
 * Created by Newamber on 2018/4/7.
 */
abstract class BaseActivity<V : BaseView, P : BasePresenter<V>> : DaggerAppCompatActivity(),
    View.OnClickListener {

    protected abstract var presenter: P
    protected open var enableRxBus = false
    private var rxBinder: ApolloBinder? = null

    // lifecycle method
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        ActivityController.addCurrent(this)
        presenter.attachView(this as V)
        if (enableRxBus) rxBinder = Apollo.bind(this)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        ActivityController.removeCurrent()
        if (enableRxBus) {
            rxBinder?.unbind()
            rxBinder = null
        }
    }

    // sub class method
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    @MenuRes
    protected open fun getMenuId() = R.menu.menu_genernal

    protected open fun initView() {}

    protected open fun processClickEvent(@IdRes viewId: Int) {}

    protected open fun processMenuItemClickEvent(@IdRes itemId: Int?) {}

    protected fun setOnClickListeners(vararg views: View) {
        views.forEach { it.setOnClickListener(this) }
    }

    final override fun onClick(v: View) = processClickEvent(v.id)

    final override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(getMenuId(), menu)
        return true
    }

    final override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        processMenuItemClickEvent(item?.itemId)
        return true
    }

}