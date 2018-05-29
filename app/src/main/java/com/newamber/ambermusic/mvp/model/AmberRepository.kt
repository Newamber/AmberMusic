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

package com.newamber.ambermusic.mvp.model

import com.newamber.ambermusic.base.BaseRepository
import com.newamber.ambermusic.mvp.model.local.*
import com.newamber.ambermusic.mvp.model.remote.LastFMAlbum
import com.newamber.ambermusic.mvp.model.remote.LastFMArtist
import com.newamber.ambermusic.mvp.model.remote.api.LastFMRestClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Description: .
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/19.
 */
class AmberRepository : BaseRepository {

    override fun getAllSongs(): Observable<MutableList<Song>> = SongLoader
        .getAllSongs()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun getSongsByAlbum(albumId: Long): Observable<MutableList<Song>> = SongLoader
        .getSongsByAlbum(albumId)

    override fun getSongsByArtist(artistId: Long): Observable<MutableList<Song>> = SongLoader
        .getSongsByArtist(artistId)

    override fun getAllAlbums(): Observable<MutableList<Album>> = AlbumLoader.getAllAlbums()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun getAlbumsByArtist(artistId: Long): Observable<MutableList<Album>> = AlbumLoader
        .getAlbums(artistId)

    override fun getAllArtists(): Observable<MutableList<Artist>> = ArtistLoader.getAllArtists()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun getArtistInfo(artistName: String, language: String?): Observable<LastFMArtist> =
        LastFMRestClient().lastFMService
            .getArtistInfo(artistName, language)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getAlbumInfo(
        albumName: String,
        artistName: String,
        language: String?
    ): Observable<LastFMAlbum> = LastFMRestClient()
        .lastFMService
        .getAlbumInfo(albumName, artistName, language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun getLocalAlbumArtwork(song: Song) = AlbumLoader.getAlbumArtwork(song.albumId)

    override fun getLocalAlbumArtwork(album: Album) = AlbumLoader.getAlbumArtwork(album.id)

//    override fun getNetArtistArtwork(artist: Artist): String {
//        var url = EMPTY_STRING
//        getArtistInfo(artist.name, null).subscribe {
//            url = getLargestArtworkUrl(it.artist.artworks)
//        }
//        return url
//    }

}
