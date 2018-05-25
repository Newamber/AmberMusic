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

package com.newamber.ambermusic.mvp.model.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.newamber.ambermusic.constants.EMPTY_STRING

/**
 * Created by Newamber(newamber@live.cn) on 2018/5/19.
 */
data class LastFMArtist(
    @Expose @SerializedName("artist") val artist: FMArtist
)

data class LastFMAlbum(
    @Expose @SerializedName("name") val albumName: String,
    @Expose @SerializedName("image") val artworks: List<Artwork>
)

// -------------------------------------------------------------------------------------------------

data class FMArtist(
    @Expose @SerializedName("name") val artistName: String,
    @Expose @SerializedName("image") val artworks: List<Artwork>,
    @Expose @SerializedName("bio") val biography: Bio
)

data class Bio(
    @Expose @SerializedName("summary") val bioSummary: String = EMPTY_STRING,
    @Expose @SerializedName("content") val bioContent: String = EMPTY_STRING
)

data class Artwork(
    @Expose @SerializedName("#text") val artUrl: String = EMPTY_STRING,
    @Expose @SerializedName("size") val artSize: String = EMPTY_STRING  // small, medium, large，extralarge, mega, ""
)
