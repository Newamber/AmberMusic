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

import com.newamber.ambermusic.di.FragmentScope
import com.newamber.ambermusic.mvp.ui.fragment.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Description: .
 * Created by Newamber on 2018/5/7.
 */
@Module
abstract class FragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector(modules = [BaseFragmentModule::class])
    abstract fun bindSongFragment(): SongFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [BaseFragmentModule::class])
    abstract fun bindAlbumFragment(): AlbumFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [BaseFragmentModule::class])
    abstract fun bindArtistFragment(): ArtistFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [BaseFragmentModule::class])
    abstract fun bindPlaylistFragment(): PlaylistFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [BaseFragmentModule::class])
    abstract fun bindPlaybackMainFragment(): PlaybackMainFragment

}