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

package com.newamber.ambermusic.mvp.model.local

import android.database.Cursor
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Artists
import com.newamber.ambermusic.AmberMusic
import com.newamber.ambermusic.constants.EMPTY_STRING
import io.reactivex.Observable

/**
 * Description: .
 * Created by Newamber(newamber@live.cn) on 2018/5/14.
 */
object ArtistLoader {

    private val projection = arrayOf(
        Artists._ID,                // 0
        Artists.ARTIST,             // 1
        Artists.NUMBER_OF_ALBUMS,   // 2
        Artists.NUMBER_OF_TRACKS    // 3
    )

    fun getAllArtists() = getArtists(makeArtistCursor())

    fun getArtists(cursor: Cursor?): Observable<MutableList<Artist>> = Observable.create {
        val artists = mutableListOf<Artist>()
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    artists.add(getArtist(it))
                } while (it.moveToNext())
            }
        }
        it.onNext(artists)
        it.onComplete()
    }

    fun getArtistArtwork() {}

    private fun getArtist(cursor: Cursor) = Artist(
        cursor.getInt(0),      // artist id
        cursor.getString(1),   // artist name
        cursor.getInt(2),      // album number of this artist
        cursor.getInt(3)       // song number of this artist
    )

    private fun makeArtistCursor(
        selection: String = EMPTY_STRING,
        selectionArgs: Array<String>? = null,
        sortOrder: String = Artists.DEFAULT_SORT_ORDER
    ): Cursor? {
        var selections = "${Artists.ARTIST} != ''"
        if (!selection.isEmpty()) selections = "$selections AND $selection"
        return AmberMusic.APP.contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            projection,
            selections,
            selectionArgs,
            sortOrder
        )
    }
}