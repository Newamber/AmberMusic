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

package com.newamber.ambermusic.mvp.model.remote.api

import com.newamber.ambermusic.constants.LASTFM_BASE_REQUEST_PATH
import com.newamber.ambermusic.mvp.model.remote.LastFMAlbum
import com.newamber.ambermusic.mvp.model.remote.LastFMArtist
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Description: RESTFul API (by [Last.fm](https://www.last.fm/api)) definition.
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/13.
 */
interface LastFMService {

    @GET("$LASTFM_BASE_REQUEST_PATH&method=artist.getinfo")
    fun getArtistInfo(
        @Query("artist") artistName: String,
        @Query("lang") language: String?
    ): Observable<LastFMArtist>

    @GET("$LASTFM_BASE_REQUEST_PATH&method=album.getinfo")
    fun getAlbumInfo(
        @Query("album") albumName: String,
        @Query("artist") artistName: String,
        @Query("lang") language: String?
    ): Observable<LastFMAlbum>
}