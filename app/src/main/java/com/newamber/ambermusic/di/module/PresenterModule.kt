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

import com.newamber.ambermusic.base.BasePresenter
import com.newamber.ambermusic.base.BaseView
import com.newamber.ambermusic.base.SimplePresenter
import com.newamber.ambermusic.di.ActivityScope
import com.newamber.ambermusic.di.FragmentScope
import com.newamber.ambermusic.di.PresenterType
import com.newamber.ambermusic.mvp.contract.*
import com.newamber.ambermusic.mvp.presenter.*
import dagger.Module
import dagger.Provides

/**
 * Description: .
 * Created by Newamber on 2018/4/12.
 */
@Module
class PresenterModule {

    @Provides
    @ActivityScope
    fun provideMainPresenter(): MainContract.Presenter = MainPresenter()

    @Provides
    @PresenterType("Activity")
    @ActivityScope
    fun provideSimplePresenter(): BasePresenter<BaseView> = SimplePresenter()

    @Provides
    @PresenterType("Fragment")
    @FragmentScope
    fun provideFSimplePresenter(): BasePresenter<BaseView> = SimplePresenter()

    @Provides
    @FragmentScope
    fun provideSongPresenter(): SongContract.Presenter = SongPresenter()

    @Provides
    @FragmentScope
    fun provideAlbumPresenter(): AlbumContract.Presenter = AlbumPresenter()

    @Provides
    @FragmentScope
    fun provideArtistPresenter(): ArtistContract.Presenter = ArtistPresenter()

    @Provides
    @FragmentScope
    fun providePlaybackMainPresenter(): PlaybackMainContract.Presenter = PlaybackMainPresenter()
}