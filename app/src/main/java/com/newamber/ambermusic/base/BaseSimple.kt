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

import com.newamber.ambermusic.di.PresenterType
import javax.inject.Inject

/**
 * Description: For Activities which have really simple business logic and do
 * not need IPresenter or IView.
 *
 * Created by Newamber(newamber@live.cn) on 2018/4/17.
 */
abstract class SimpleActivity : BaseActivity<BaseView, BasePresenter<BaseView>>()
    , BaseView {

    @Inject
    @field:PresenterType("Activity")
    override lateinit var presenter: BasePresenter<BaseView>
}

/**
 * The same desc. as [SimpleActivity].
 *
 * Created by Newamber(newamber@live.cn) on 2018/4/17.
 */
abstract class SimpleFragment : BaseFragment<BaseView, BasePresenter<BaseView>>()
    , BaseView {

    @Inject
    @field:PresenterType("Fragment")
    override lateinit var presenter: BasePresenter<BaseView>

}

/**
 * Description: For simple Fragment and Activity.
 *
 * Created by Newamber(newamber@live.cn) on 2018/4/19.
 */
class SimplePresenter : BasePresenter<BaseView>()