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

import android.support.v7.widget.LinearLayoutManager
import com.newamber.ambermusic.R
import com.newamber.ambermusic.base.BaseFragment
import com.newamber.ambermusic.mvp.adpter.SongListAdapter
import com.newamber.ambermusic.mvp.contract.SongContract
import com.newamber.ambermusic.util.post
import kotlinx.android.synthetic.main.fragment_song.*
import javax.inject.Inject

/**
 * Description: .
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/7.
 */
class SongFragment : BaseFragment<SongContract.View, SongContract.Presenter>(),
    SongContract.View {

    @Inject
    override lateinit var presenter: SongContract.Presenter

    override fun getLayoutId() = R.layout.fragment_song

    //override fun enableRxBus() = true

    override fun initView() {

    }

    override fun initData() {
        val songAdapter = SongListAdapter()
        with(recyclerViewFSong) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = songAdapter
        }
        presenter.refreshSongsBy(songAdapter)
        songAdapter.setOnClickListener { _, song, _ ->
            post("event_play_song_main_activity", arrayOf(song, songAdapter.getEntityList()))
        }
    }

}