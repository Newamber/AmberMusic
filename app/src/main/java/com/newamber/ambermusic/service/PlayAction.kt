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

package com.newamber.ambermusic.service

import com.newamber.ambermusic.mvp.model.local.Song
import java.util.*

/**
 * Description: .
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/21.
 */
interface PlayAction {

    fun previous(song: Song?, songList: List<Song>, isUserAction: Boolean): Song?

    fun next(song: Song?, songList: List<Song>, isUserAction: Boolean): Song?

    fun clear()
}

object SingleLoopPlay : PlayAction {

    override fun previous(song: Song?, songList: List<Song>, isUserAction: Boolean): Song? {
        if (isUserAction) return ListLoopPlay.previous(song, songList, isUserAction)
        return song
    }

    override fun next(song: Song?, songList: List<Song>, isUserAction: Boolean): Song? {
        if (isUserAction) return ListLoopPlay.next(song, songList, isUserAction)
        return song
    }

    override fun clear() {}
}

object ListLoopPlay : PlayAction {
    override fun previous(song: Song?, songList: List<Song>, isUserAction: Boolean): Song? {
        if (!songList.isEmpty()) {
            if (song == null) return songList[0]
            val index = songList.indexOf(song)
            if (index < 0)
                return songList[0]
            else if (index == 0)
                return songList[songList.size - 1]
        }
        return song
    }

    override fun next(song: Song?, songList: List<Song>, isUserAction: Boolean): Song? {
        if (!songList.isEmpty()) {
            if (song == null) return songList[0]
            val index = songList.indexOf(song)
            if (index < 0) return songList[0]
            return songList[(index + 1) % songList.size]
        }
        return song
    }

    override fun clear() {}
}

object RandomPlay : PlayAction {

    private val random by lazy { Random() }
    private val stack by lazy { Stack<Song>() }

    override fun previous(song: Song?, songList: List<Song>, isUserAction: Boolean): Song? {
        if (!songList.isEmpty()) {
            return song
        }
        if (!stack.isEmpty()) {
            return stack.pop()
        }
        val index = random.nextInt(songList.size)
        return songList[index]
    }

    override fun next(song: Song?, songList: List<Song>, isUserAction: Boolean): Song? {
        if (songList.size > 1) {
            var forwardSong: Song
            if (!stack.isEmpty()) {
                val lastSong = stack[stack.size - 1]
                do {
                    val index = random.nextInt(songList.size)
                    forwardSong = songList[index]
                } while (lastSong == forwardSong)
            } else {
                val index = random.nextInt(songList.size)
                forwardSong = songList[index]
            }
            stack.push(forwardSong)
            return forwardSong
        }
        return song
    }

    override fun clear() {
        stack.clear()
    }
}