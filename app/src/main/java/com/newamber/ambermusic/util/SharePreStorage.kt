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

import android.content.Context
import com.newamber.ambermusic.AmberMusic
import com.newamber.ambermusic.constants.SHARE_PREFERENCE_DEFAULT_STRING_VALUE
import com.newamber.ambermusic.constants.SHARE_PREFERENCE_FILE_NAME
import com.newamber.ambermusic.constants.SP_LAST_PLAYED_PROGRESS
import com.newamber.ambermusic.constants.SP_LAST_PLAYED_SONG
import com.newamber.ambermusic.mvp.model.local.SongLoader

/**
 * Description: .
 * Created by Newamber on 2018/4/26.
 */
@Suppress("unused")
object SharePreStorage {

    private val sp by lazy {
        AmberMusic.APP.getSharedPreferences(
            SHARE_PREFERENCE_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }

    private val editor = sp.edit()

    fun saveLastPlayedSongId(songId: Long) = put(SP_LAST_PLAYED_SONG, songId)

    fun getLastPlayedSongId() = getLong(SP_LAST_PLAYED_SONG, SongLoader.getFirstSongId())

    fun saveLastPlayedProgress(progress: Int) = put(SP_LAST_PLAYED_PROGRESS, progress)

    fun getLastPlayedProgress() = getInt(SP_LAST_PLAYED_PROGRESS)

    // setter
    fun put(key: String, value: Int) = editor.putInt(key, value).apply()

    fun put(key: String, value: Long) = editor.putLong(key, value).apply()

    fun put(key: String, value: Float) = editor.putFloat(key, value).apply()

    fun put(key: String, value: Boolean) = editor.putBoolean(key, value).apply()

    fun put(key: String, value: String) = editor.putString(key, value).apply()

    fun put(key: String, value: Set<String>) = editor.putStringSet(key, value).apply()

    // getter
    fun getInt(key: String, default: Int = 0) = sp.getInt(key, default)

    fun getLong(key: String, default: Long = 0) = sp.getLong(key, default)

    fun getFloat(key: String, default: Float = 0f) = sp.getFloat(key, default)

    fun getBoolean(key: String, default: Boolean = false) = sp.getBoolean(key, default)

    fun getString(key: String, default: String = SHARE_PREFERENCE_DEFAULT_STRING_VALUE): String =
        sp.getString(key, default)

    fun getStringSet(key: String, default: Set<String> = setOf()): MutableSet<String> =
        sp.getStringSet(key, default)

    // other
    fun contains(key: String) = sp.contains(key)

    fun remove(key: String) = editor.remove(key).apply()

    fun clear() = editor.clear().apply()

}
