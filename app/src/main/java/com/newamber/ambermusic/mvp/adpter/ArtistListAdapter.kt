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
import com.newamber.ambermusic.mvp.model.local.Artist
import com.newamber.ambermusic.util.findString
import com.newamber.ambermusic.util.getLargestArtworkUrl
import com.newamber.ambermusic.util.hasArtwork
import com.newamber.ambermusic.util.logDebug
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView

/**
 * Description: .
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/19.
 */
class ArtistListAdapter(
    entityList: MutableList<Artist> = mutableListOf(),
    @LayoutRes layoutId: Int = R.layout.item_artist_card,
    diffRuleCallBack: BaseDiffRuleCallBack<Artist> = object : BaseDiffRuleCallBack<Artist>() {
        override fun areItemsTheSame(oldItem: Artist?, newItem: Artist?) =
            oldItem?.id == newItem?.id
    }
) : BaseRecyclerViewAdapter<Artist>(entityList, layoutId, diffRuleCallBack),
    FastScrollRecyclerView.SectionedAdapter {

    override fun convertView(holder: BaseViewHolder, entity: Artist) {
        with(holder) {
            setText(R.id.textViewNameFArtist, entity.name)
            setText(
                R.id.textViewCountFArtist,
                "${entity.albumCount} ${findString(R.string.album)} ● ${entity.songCount} ${findString(
                    R.string.song
                )}"
            )
            repository.getArtistInfo(entity.name, null).subscribe(
                {
                    if (hasArtwork(it.artist.artworks)) {
                        setImage(R.id.imageViewFArtist, getLargestArtworkUrl(it.artist.artworks))
                    } else {
                        setImage(R.id.imageViewFArtist, R.drawable.bg_navigation)
                    }
                },
                { logDebug(it) }
            )
        }
    }

    override fun getSectionName(position: Int) = getEntity(position).name[0].toString()

}