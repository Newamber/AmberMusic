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

package com.newamber.ambermusic.util

import com.newamber.ambermusic.constants.EMPTY_STRING
import com.newamber.ambermusic.mvp.model.remote.Artwork

/**
 * Description: Get artwork url by [Artwork].
 *
 * @return the most largest size of the given artworks, if artworks have nothing
 * return ""
 *
 *  @author Newamber(newamber@live.cn)
 */
fun getArtworkUrl(artworks: List<Artwork>): String {
    var matchedArtworks = artworks.filter { it.artSize == "mega" }
    if (matchedArtworks.isEmpty()) {
        matchedArtworks = artworks.filter { it.artSize == "extralarge" }
        if (matchedArtworks.isEmpty()) {
            matchedArtworks = artworks.filter { it.artSize == "large" }
            if (matchedArtworks.isEmpty()) {
                matchedArtworks = artworks.filter { it.artSize == "medium" }
                if (matchedArtworks.isEmpty()) {
                    matchedArtworks = artworks.filter { it.artSize == "small" }
                    if (matchedArtworks.isEmpty()) {
                        return EMPTY_STRING
                    }
                }
            }
        }
    }
    return matchedArtworks[0].artUrl
}

fun getLargestArtworkUrl(artworks: List<Artwork>) = artworks
    .filter { it.artSize == "mega" }[0].artUrl

fun hasArtwork(artworks: List<Artwork>) = artworks[0].artUrl != EMPTY_STRING