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

import com.newamber.ambermusic.constants.EMPTY_STRING

/**
 * Description: Entity music class.
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/10.
 */
@Suppress("MemberVisibilityCanBePrivate")
data class Song(
    val id: Int,
    val title: String,
    val trackNumber: Int,
    val year: Int,
    val duration: Long,
    val path: String,
    val size: Long,
    val dateModified: Long,
    val albumId: Int,
    val albumName: String,
    val artistId: Int,
    val artistName: String,
    val mimeType: String
) {
    companion object {
        val EMPTY_SONG = Song(
            -1,
            EMPTY_STRING,
            -1,
            -1,
            -1L,
            EMPTY_STRING,
            -1L,
            -1L,
            -1,
            EMPTY_STRING,
            -1,
            EMPTY_STRING,
            EMPTY_STRING
        )
    }
}

data class Album(
    val id: Int,
    val name: String,
    val songCount: Int,
    val year: Int,
    val artistName: String
) {

    companion object {
        val EMPTY_ALBUM = Album(
            -1, EMPTY_STRING, -1, -1, EMPTY_STRING
        )
    }
}

data class Artist(
    val id: Int,
    val name: String,
    val albumCount: Int,
    val songCount: Int
) {
    companion object {
        val EMPTY_ARTIST = Artist(-1, EMPTY_STRING, -1, -1)
    }
}

data class Playlist(
    val id: Int,
    val name: String,
    val songCount: Int
) {
    companion object {
        val EMPTY_PLAYLIST = Playlist(-1, EMPTY_STRING, -1)
    }
}