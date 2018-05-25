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

package com.newamber.ambermusic.base

import android.net.Uri
import com.newamber.ambermusic.mvp.model.local.Album
import com.newamber.ambermusic.mvp.model.local.Artist
import com.newamber.ambermusic.mvp.model.local.Song
import com.newamber.ambermusic.mvp.model.remote.LastFMAlbum
import com.newamber.ambermusic.mvp.model.remote.LastFMArtist
import io.reactivex.Observable

/**
 * Description: .
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/19.
 */
interface BaseRepository {

    fun getAllSongs(): Observable<MutableList<Song>>

    fun getAllAlbums(): Observable<MutableList<Album>>

    fun getAllArtists(): Observable<MutableList<Artist>>

    fun getArtistInfo(artistName: String, language: String?): Observable<LastFMArtist>

    fun getAlbumInfo(
        albumName: String,
        artistName: String,
        language: String?
    ): Observable<LastFMAlbum>

    fun getLocalAlbumArtwork(song: Song): Uri

    fun getLocalAlbumArtwork(album: Album): Uri

    //fun getNetArtistArtwork(artist: Artist): String
}