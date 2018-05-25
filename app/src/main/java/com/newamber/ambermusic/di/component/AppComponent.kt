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

package com.newamber.ambermusic.di.component

import com.newamber.ambermusic.AmberMusic
import com.newamber.ambermusic.di.module.ActivityBuilder
import com.newamber.ambermusic.di.module.AppModule
import com.newamber.ambermusic.di.module.FragmentBuilder
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Description: This is a Dagger component. Refer to [com.newamber.ambermusic.AmberMusic]
 * for the list of Dagger components used in this application.
 *
 * Even though Dagger allows annotating a `Component` as a singleton, the code
 * itself must ensure only one instance of the class is created.
 * This is done in [com.newamber.ambermusic.AmberMusic].
 *
 * [AndroidSupportInjectionModule] is the module from Dagger.
 * Android that helps with the generation and location of sub components.
 *
 * Created by Newamber on 2018/4/12.
 */
@Singleton
@Component(
    modules = [
        AppModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBuilder::class,
        FragmentBuilder::class
    ]
)
interface AppComponent : AndroidInjector<AmberMusic> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<AmberMusic>()
}