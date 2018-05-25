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

package com.newamber.ambermusic.mvp.adpter

import android.support.annotation.LayoutRes
import com.newamber.ambermusic.R
import com.newamber.ambermusic.base.BaseDiffRuleCallBack
import com.newamber.ambermusic.base.BaseRecyclerViewAdapter
import com.newamber.ambermusic.base.BaseViewHolder
import com.newamber.ambermusic.mvp.model.local.Song
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView

/**
 * Description: .
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/14.
 */
class SongListAdapter(
    entityList: MutableList<Song> = mutableListOf(),
    @LayoutRes layoutId: Int = R.layout.item_song,
    diffRuleCallBack: BaseDiffRuleCallBack<Song> = object : BaseDiffRuleCallBack<Song>() {
        override fun areItemsTheSame(oldItem: Song?, newItem: Song?) =
            oldItem?.id == newItem?.id
    }
) : BaseRecyclerViewAdapter<Song>(entityList, layoutId, diffRuleCallBack),
    FastScrollRecyclerView.SectionedAdapter {

    override fun convertView(holder: BaseViewHolder, entity: Song) {
        with(holder) {
            setImage(R.id.imageViewArtFSong, repository.getLocalAlbumArtwork(entity))
            setImage(R.id.buttonMoreFSong, R.drawable.ic_overflow)
            setText(R.id.textViewTitleFSong, entity.title)
            setText(R.id.textViewArtistNameFSong, entity.artistName)
        }
    }

    override fun getSectionName(position: Int) = getEntity(position).title[0].toString()

}