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

package com.newamber.ambermusic.di.module

import com.newamber.ambermusic.BuildConfig
import com.newamber.ambermusic.constants.LOGGER_METHOD_COUNT
import com.newamber.ambermusic.constants.LOGGER_TAG
import com.newamber.ambermusic.util.LogFormatStrategy
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.LogAdapter
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Description: This is a Dagger module. Use this to bind our
 * Application class as a Context in the AppComponent by using
 * `Dagger.Android` so that we do not need to pass our Application
 * instance to any module, we simply need to expose our Application
 * as Context.
 *
 * One of the advantages of Dagger. Android is that your
 * Application & Activities are provided into your graph for you.
 *
 * All global object should be initialized here.
 *
 * @see com.newamber.ambermusic.di.component.AppComponent
 *
 * Created by Newamber on 2018/4/12.
 */
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideLogAdapter(): LogAdapter {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .logStrategy(LogFormatStrategy())
            .methodCount(LOGGER_METHOD_COUNT)
            .tag(LOGGER_TAG)
            .build()

        return object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?) = BuildConfig.DEBUG
        }
    }
}