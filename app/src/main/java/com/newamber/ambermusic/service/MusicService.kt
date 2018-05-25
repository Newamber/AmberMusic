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

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.newamber.ambermusic.mvp.model.local.Song
import com.newamber.ambermusic.mvp.ui.activity.MainActivity
import com.newamber.ambermusic.util.logDebug
import com.newamber.ambermusic.util.toUri
import java.io.IOException

/**
 * Description: Music Service.
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/20.
 */
class MusicService : Service(),
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener {

    private var stateListener: OnPlayStateChangeListener? = null
    private var player: MediaPlayer? = null
    private val binder by lazy { MusicPlayBinder() }
    private var curplayState = PlayState.IDLE
    private lateinit var notifyManager: NotificationManagerCompat

    var isIDLE = curplayState == PlayState.IDLE
    var isInitialized = curplayState == PlayState.INITIALIZED
    var isError = curplayState == PlayState.ERROR
    var isPrepared = curplayState == PlayState.PREPARED
    var isStarted = curplayState == PlayState.STARTED
    var isPaused = curplayState == PlayState.PAUSED
    var isStopped = curplayState == PlayState.STOPPED
    var isCompleted = curplayState == PlayState.COMPLETED

    fun setOnPlayStateChangeListener(listener: OnPlayStateChangeListener?) {
        stateListener = listener
    }

    fun preparePlay(path: String) {
        initPlayer()
        try {
            player?.apply {
                reset()
                setPlayState(PlayState.IDLE)
                if (path.startsWith("content://")) {
                    setDataSource(this@MusicService, path.toUri())
                } else {
                    setDataSource(path)
                }
                setPlayState(PlayState.INITIALIZED)
                val attr = AudioAttributes
                    .Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                setAudioAttributes(attr)
                prepareAsync()
                setPlayState(PlayState.PREPARING)
            }
        } catch (e: IOException) {
            logDebug(e)
        }
    }

    fun startPlay() {
        if (isPrepared || isPaused || isCompleted || isStarted) {
            player?.start()
            setPlayState(PlayState.STARTED)
        }
    }

    fun pause() {
        if (isStarted) player?.pause()
        setPlayState(PlayState.PAUSED)
    }

    fun stop() {
        if (isPrepared || isStarted || isPaused || isStopped || isCompleted) {
            player?.stop()
            setPlayState(PlayState.STOPPED)
        }
    }

    fun release() {
        if (isIDLE) player?.release()
        player = null
        setPlayState(PlayState.END)
    }

    fun seekTo(position: Int) {
        if (isPrepared || isStarted || isPaused || isCompleted) {
            player?.seekTo(position) // seekTo() does not change play state
        }
    }

    fun resume() {
        if (isPaused) startPlay()
    }

    fun updateNotification(song: Song?) {
        notifyManager.notify(0, buildNotification(song))
    }

    /**
     * @return milliseconds
     */
    fun getPosition(): Int {
        player?.apply {
            return if (!isInitialized) {
                -1
            } else {
                try {
                    currentPosition
                } catch (e: Exception) {
                    logDebug(e)
                    -1
                }
            }
        }
        return -1
    }

    // lifecycle method
    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
        notifyManager = NotificationManagerCompat.from(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = Service.START_STICKY

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
        stateListener?.onShutdown()
    }

    // media listener
    override fun onPrepared(mp: MediaPlayer?) {
        setPlayState(PlayState.PREPARED)
        startPlay()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        setPlayState(PlayState.COMPLETED)
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        setPlayState(PlayState.ERROR)
        return false
    }

    // nested class & interface
    enum class PlayState {
        IDLE,
        INITIALIZED,
        ERROR,
        PREPARING,
        PREPARED,
        STARTED,
        PAUSED,
        STOPPED,
        COMPLETED,
        END
    }

    interface OnPlayStateChangeListener {

        fun onStateChanged(state: PlayState)

        fun onShutdown()
    }

    inner class MusicPlayBinder : Binder() {
        fun getService() = this@MusicService
    }

    // private
    private fun setPlayState(state: PlayState) {
        if (curplayState == state) return
        curplayState = state
        stateListener?.onStateChanged(curplayState)
    }

    private fun initPlayer() {
        if (player == null) player = MediaPlayer()
        setPlayState(PlayState.IDLE)
        player?.apply {
            val context = this@MusicService
            setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            setOnPreparedListener(context)
            setOnCompletionListener(context)
            setOnErrorListener(context)
        }

    }

    private fun buildNotification(song: Song?): Notification {
        val songName = song?.title
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        return NotificationCompat.Builder(this)
            .setContentTitle("Amber Music")
            .setContentText(songName)
            .setContentIntent(pi)
            .build()
    }

}