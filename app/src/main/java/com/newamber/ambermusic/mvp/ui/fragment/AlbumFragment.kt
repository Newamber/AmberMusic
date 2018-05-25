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

package com.newamber.ambermusic.mvp.ui.fragment

import android.support.v7.widget.GridLayoutManager
import com.newamber.ambermusic.R
import com.newamber.ambermusic.base.BaseFragment
import com.newamber.ambermusic.mvp.adpter.AlbumListAdapter
import com.newamber.ambermusic.mvp.contract.AlbumContract
import kotlinx.android.synthetic.main.fragment_album.*
import javax.inject.Inject

/**
 * Created by Newamber(newmber@live.cn) on 2018/5/7.
 */
class AlbumFragment : BaseFragment<AlbumContract.View, AlbumContract.Presenter>(),
    AlbumContract.View {

    @Inject
    override lateinit var presenter: AlbumContract.Presenter

    override fun getLayoutId() = R.layout.fragment_album

    override fun initData() {
        val albumAdapter = AlbumListAdapter()
        with(recyclerViewFAlbum) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            adapter = albumAdapter
        }
        presenter.refreshAlbumsBy(albumAdapter)
    }
}