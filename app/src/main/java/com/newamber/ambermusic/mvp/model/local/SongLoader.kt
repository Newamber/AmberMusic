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
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import android.provider.MediaStore.Audio.Media
import com.newamber.ambermusic.AmberMusic
import com.newamber.ambermusic.constants.EMPTY_STRING
import io.reactivex.Observable

/**
 * Description: .
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/11.
 */
object SongLoader {

    private val projection = arrayOf(
        AudioColumns._ID,            // 0
        AudioColumns.TITLE,          // 1
        AudioColumns.TRACK,          // 2
        AudioColumns.YEAR,           // 3
        AudioColumns.DURATION,       // 4
        AudioColumns.DATA,           // 5
        AudioColumns.SIZE,           // 6
        AudioColumns.DATE_MODIFIED,  // 7
        AudioColumns.ALBUM_ID,       // 8
        AudioColumns.ALBUM,          // 9
        AudioColumns.ARTIST_ID,      // 10
        AudioColumns.ARTIST,         // 11
        AudioColumns.MIME_TYPE       // 12
    )

    fun getAllSongs() = getSongs(makeSongCursor())

//    fun getSongs(query: String) = getSongs(
//        makeSongCursor(
//            "${AudioColumns.TITLE} LIKE ?",
//            arrayOf("%$query%")
//        )
//    )

    fun getSongsByAlbum(albumId: Long) = getSongs(
        makeSongCursor("${AudioColumns.ALBUM_ID} = ?", arrayOf("$albumId"))
    )

    fun getSongsByArtist(artistId: Long) = getSongs(
        makeSongCursor("${AudioColumns.ARTIST_ID} = ?", arrayOf("$artistId"))
    )

    fun getSongUri(songId: Long): Uri = ContentUris
        .withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId)

    // ---------------------------------------private-----------------------------------------------
    private fun getSongs(cursor: Cursor?): Observable<MutableList<Song>> = Observable.create {
        val songs = mutableListOf<Song>()
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    songs.add(getSong(it))
                } while (it.moveToNext())
            }
        }
        it.onNext(songs)
        it.onComplete()
    }

    fun getFirstSongId(): Long {
        var song = Song.EMPTY_SONG
        makeSongCursor()?.apply {
            if (moveToFirst()) song = getSong(this)
            close()
        }
        return song.id
    }

    fun getSong(songId: Long): Song {
        var song = Song.EMPTY_SONG
        makeSongCursor(
            "${AudioColumns._ID} = ?",
            arrayOf("$songId")
        )?.apply {
            if (moveToFirst()) song = getSong(this)
            close()
        }
        return song
    }

    private fun getSong(cursor: Cursor) = Song(
        cursor.getLong(0),    // id
        cursor.getString(1),  // title
        cursor.getInt(2),     // track number
        cursor.getInt(3),     // year
        cursor.getLong(4),    // duration
        cursor.getString(5),  // path
        cursor.getLong(6),    // size
        cursor.getLong(7),    // date modified
        cursor.getLong(8),    // album id
        cursor.getString(9),  // album name
        cursor.getLong(10),   // artist id
        cursor.getString(11), // artist name
        cursor.getString(12)  // mime type
    )

    private fun makeSongCursor(
        selection: String = EMPTY_STRING,
        selectionArgs: Array<String>? = null,
        sortOrder: String = Media.DEFAULT_SORT_ORDER
    ): Cursor? {
        var selections = "${AudioColumns.IS_MUSIC} > 0 AND ${AudioColumns.TITLE} != ''"
        if (!selection.isEmpty()) selections = "$selections AND $selection"
        return AmberMusic.APP.contentResolver.query(
            Media.EXTERNAL_CONTENT_URI,
            projection,
            selections,
            selectionArgs,
            sortOrder
        )
    }

}