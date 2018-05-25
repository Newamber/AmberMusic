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

package com.newamber.ambermusic

import com.lsxiao.apollo.core.Apollo
import com.newamber.ambermusic.di.component.DaggerAppComponent
import com.newamber.ambermusic.util.CrashHandler
import com.orhanobut.logger.LogAdapter
import com.orhanobut.logger.Logger
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Description:
 *
 * Created by Newamber on 2018/4/5.
 */
class AmberMusic : DaggerApplication() {

    @Inject
    lateinit var logAdapter: LogAdapter

    override fun onCreate() {
        super.onCreate()
        APP = this
        init()
    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().create(this)


    companion object {
        var APP: AmberMusic by Delegates.notNull()
            private set
    }

    private fun init() {
        Apollo.init(AndroidSchedulers.mainThread(), this)
        Logger.addLogAdapter(logAdapter)
        CrashHandler.init()
    }
}