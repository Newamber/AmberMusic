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

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore.Audio.Albums
import com.newamber.ambermusic.AmberMusic
import com.newamber.ambermusic.constants.EMPTY_STRING
import com.newamber.ambermusic.util.toUri
import io.reactivex.Observable


/**
 * Created by Newamber(newamber@live.cn) on 2018/5/13.
 */
object AlbumLoader {

    private val projection = arrayOf(
        Albums._ID,               // 0
        Albums.ALBUM,             // 1
        Albums.NUMBER_OF_SONGS,   // 2
        Albums.FIRST_YEAR,        // 3
        Albums.ARTIST,            // 4
        Albums.ALBUM_ART          // NOTE: Do not use this column or lead to ANR!
    )

    fun getAllAlbums() = getAlbums(makeAlbumCursor())

    fun getAlbum(cursor: Cursor?): Observable<Album> = Observable.create {
        var album = Album.EMPTY_ALBUM
        cursor?.apply {
            if (moveToFirst()) album = getAlbum(this)
            close()
        }
        it.onNext(album)
        it.onComplete()
    }

    fun getAlbumArtwork(albumId: Int): Uri = ContentUris
        .withAppendedId("content://media/external/audio/albumart".toUri(), albumId.toLong())


    private fun getAlbums(cursor: Cursor?): Observable<MutableList<Album>> = Observable.create {
        val albums = mutableListOf<Album>()
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    albums.add(getAlbum(it))
                } while (it.moveToNext())
            }
        }
        it.onNext(albums)
        it.onComplete()
    }

    private fun getAlbum(cursor: Cursor) = Album(
        cursor.getInt(0),      // album id
        cursor.getString(1),   // album name
        cursor.getInt(2),      // song count
        cursor.getInt(3),      // year(earliest)
        cursor.getString(4)    // artist name
    )

    private fun makeAlbumCursor(
        selection: String = EMPTY_STRING,
        selectionArgs: Array<String>? = null,
        sortOrder: String = Albums.DEFAULT_SORT_ORDER
    ): Cursor? {
        var selections = "${Albums.ALBUM} != ''"
        if (!selection.isEmpty()) selections = "$selections AND $selection"
        return AmberMusic.APP.contentResolver.query(
            Albums.EXTERNAL_CONTENT_URI,
            projection,
            selections,
            selectionArgs,
            sortOrder
        )
    }
}