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

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.IBinder
import android.support.v4.app.NotificationManagerCompat
import com.newamber.ambermusic.mvp.model.local.Song
import com.newamber.ambermusic.util.post

/**
 * Created by Newamber(newamber@live.cn) on 2018/5/21.
 */
class PlaybackManager(private val context: Context) : MusicService.OnPlayStateChangeListener {

    private lateinit var playService: MusicService
    private var isPausedByUser = false
    private var currentList = listOf<Song>()
    private var currentSong: Song? = null
    private var playAction: PlayAction = ListLoopPlay
    var isPlaying = playService.isStarted
    var isPaused = playService.isPaused

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playService = (service as MusicService.MusicPlayBinder).getService()
            playService.setOnPlayStateChangeListener(this@PlaybackManager)
            startRemoteControl()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playService.setOnPlayStateChangeListener(null)
        }
    }

    private val audioListener = AudioManager.OnAudioFocusChangeListener {
        when (it) {
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS -> {
                if (isPlaying) pause(false)
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (isPaused && !isPausedByUser) resume()
            }
        }
    }

    fun next(isUserAction: Boolean = true) {
        play(playAction.next(currentSong, currentList, isUserAction))
    }

    fun previous(isUserAction: Boolean = true) {
        play(playAction.previous(currentSong, currentList, isUserAction))
    }

    fun seekTo(position: Int) {
        playService.seekTo(position)
    }

    fun setPlayAction(action: PlayAction) {
        playAction = action
        // TODO: post()
    }

    fun initSongList(songList: List<Song>) {
        currentList = songList
    }

    fun updateNotification() {
        playService.updateNotification(currentSong)
    }

    fun getCurrentPosition() = playService.getPosition()

    fun pause(isPausedByUser: Boolean = true) {
        playService.pause()
        this.isPausedByUser = isPausedByUser
    }

    fun resume() {
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == requestAudioFocus()) {
            playService.resume()
        }
    }

    fun play(song: Song? = currentSong) {
        if (currentList.isEmpty() || song == null) return
        if (currentSong == null) {
            val defaultSong = playAction.next(song, currentList, false)
            play(defaultSong)
        } else if (song == currentSong) {
            if (isPlaying) {
                pause()
            } else if (isPaused) {
                resume()
            } else {
                playService.release()
                if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == requestAudioFocus()) {
                    currentSong = song
                    playService.preparePlay(song.path)
                }
            }
        } else {
            playService.release()
            if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == requestAudioFocus()) {
                currentSong = song
                playService.preparePlay(song.path)
            }
        }
        post("event_play_song_to_playback_fragment", song)
    }

    fun release() {
        playService.release()
        unBndService()
        stopService()
        playService.setOnPlayStateChangeListener(null)
    }

    fun bindService() {
        context.bindService(
            Intent(context, MusicService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun unBndService() {
        context.unbindService(connection)
    }

    fun startService() {
        context.startService(Intent(context, MusicService::class.java))
    }

    fun stopService() {
        context.stopService(Intent(context, MusicService::class.java))
    }

    override fun onStateChanged(state: MusicService.PlayState) {
        when (state) {
            MusicService.PlayState.IDLE -> isPausedByUser = false

            MusicService.PlayState.ERROR -> releaseAudioFocus()
        }
    }

    override fun onShutdown() {
        releaseAudioFocus()
        playService.stopForeground(true)
        NotificationManagerCompat.from(context).cancelAll()
    }

    // private
    @Suppress("DEPRECATION")
    private fun requestAudioFocus(): Int {
        val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        return manager.requestAudioFocus(
            audioListener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
    }

    @Suppress("DEPRECATION")
    private fun releaseAudioFocus(): Int {
        val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return manager.abandonAudioFocus(audioListener)
    }

    private fun startRemoteControl() {
        // TODO
    }

}