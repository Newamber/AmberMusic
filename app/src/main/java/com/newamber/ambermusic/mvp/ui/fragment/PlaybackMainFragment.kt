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

package com.newamber.ambermusic.mvp.ui.fragment

import android.view.View
import android.widget.SeekBar
import com.lsxiao.apollo.core.annotations.Receive
import com.lsxiao.apollo.core.annotations.Sticky
import com.newamber.ambermusic.R
import com.newamber.ambermusic.base.BaseFragment
import com.newamber.ambermusic.mvp.contract.PlaybackMainContract
import com.newamber.ambermusic.mvp.model.local.Song
import com.newamber.ambermusic.mvp.ui.activity.MainActivity
import com.newamber.ambermusic.util.logDebug
import com.newamber.ambermusic.util.post
import com.newamber.ambermusic.util.toMinuteSecond
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import isVisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_drag_playback.*
import javax.inject.Inject

/**
 * Description: Playback Fragment.
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/16.
 */
class PlaybackMainFragment :
    BaseFragment<PlaybackMainContract.View, PlaybackMainContract.Presenter>(),
    PlaybackMainContract.View {

    override var enableRxBus = true

    private var isPlaying = false
    private var updateProgressFromUser = false

    @Inject
    override lateinit var presenter: PlaybackMainContract.Presenter

    override fun getLayoutId() = R.layout.fragment_drag_playback

    override fun initView() {
        (activity as MainActivity).slidingLayoutMain.addPanelSlideListener(object :
            SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                with(imageViewPlayerArtworkMain) {
                    alpha = slideOffset
                    isVisible = (slideOffset != 0f)
                }
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {

            }
        })
    }

    override fun setMultiListener() {
        super.setMultiListener()
        setOnClickListeners(buttonPlayMain, fabPlayMain, buttonNextSongMain, buttonPreSongMain)
        seekBarMain.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateProgressFromUser = fromUser
                textViewCurrentTime.text = progress.toMinuteSecond()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.apply {
                    post("event_update_audio_progress", progress)
                    progressBarMain.progress = progress
                }
                updateProgressFromUser = false
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun processClickEvent(viewId: Int) {
        when (viewId) {
            R.id.buttonNextSongMain -> {
                post("event_play_next_song_main_activity")
                if (!isPlaying) {
                    setPauseButton()
                    isPlaying = true
                }
                restProgressAndTime()
            }
            R.id.buttonPreSongMain -> {
                post("event_play_pre_song_main_activity")
                if (!isPlaying) {
                    setPauseButton()
                    isPlaying = true
                }
                restProgressAndTime()
            }
            R.id.buttonPlayMain -> clickPlayButton()
            R.id.fabPlayMain -> clickPlayButton()
        }
    }

    @Receive("event_init_last_play_state")
    @Sticky
    fun onEventInitLastPlayState(array: Array<Any>) {
        logDebug("init")
        val song = array[0] as Song
        val progress = array[1] as Int
        with(song) {
            if (song != Song.EMPTY_SONG) {
                textViewTitleMiniMain.text = title
                setImage(imageViewPlayerArtworkMain, presenter.getArtwork(this))
                textViewTitleMain.text = title
                textViewArtistMain.text = artistName
                progressBarMain.max = duration.toInt()
                progressBarMain.progress = progress
                seekBarMain.max = duration.toInt()
                seekBarMain.progress = progress
                textViewCurrentTime.text = progress.toMinuteSecond()
                textViewSongDuration.text = duration.toInt().toMinuteSecond()
            }
        }
    }

    @Receive("event_update_ui_playback_fragment")
    fun onEventUpdateUI(song: Song) {
        isPlaying = true
        with(song) {
            textViewTitleMiniMain.text = title
            setImage(imageViewPlayerArtworkMain, presenter.getArtwork(song))
            textViewTitleMain.text = title
            textViewArtistMain.text = artistName
            setPauseButton()
            progressBarMain.max = duration.toInt()
            seekBarMain.max = duration.toInt()
            restProgressAndTime()
            textViewSongDuration.text = duration.toInt().toMinuteSecond()
        }
    }

    @Receive("event_update_progress")
    fun onEventUpdateProgress(progress: Int) {
        if (!updateProgressFromUser) {
            progressBarMain.progress = progress
            seekBarMain.progress = progress
            textViewCurrentTime.text = progress.toMinuteSecond()
        }
    }

    private fun clickPlayButton() {
        isPlaying = if (isPlaying) {
            setPlayButton()
            post("event_pause_main_activity")
            false
        } else {
            setPauseButton()
            post("event_play_main_activity")
            true
        }
    }

    private fun setPlayButton() {
        setImage(buttonPlayMain, R.drawable.ic_play)
        setImage(fabPlayMain, R.drawable.ic_play)
    }

    private fun setPauseButton() {
        setImage(buttonPlayMain, R.drawable.ic_pause)
        setImage(fabPlayMain, R.drawable.ic_pause)
    }

    private fun restProgressAndTime() {
        val progress = 0
        progressBarMain.progress = progress
        seekBarMain.progress = progress
        textViewCurrentTime.text = progress.toMinuteSecond()
    }
}