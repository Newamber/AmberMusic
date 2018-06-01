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

package com.newamber.ambermusic.mvp.ui.activity

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.PowerManager
import android.support.design.widget.TabLayout
import android.support.design.widget.TabLayout.ViewPagerOnTabSelectedListener
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import com.lsxiao.apollo.core.annotations.Receive
import com.newamber.ambermusic.R
import com.newamber.ambermusic.R.color
import com.newamber.ambermusic.R.string
import com.newamber.ambermusic.base.BaseActivity
import com.newamber.ambermusic.base.BaseFragmentPagerAdapter
import com.newamber.ambermusic.mvp.contract.MainContract
import com.newamber.ambermusic.mvp.model.local.Song
import com.newamber.ambermusic.mvp.model.local.SongLoader
import com.newamber.ambermusic.mvp.ui.fragment.AlbumFragment
import com.newamber.ambermusic.mvp.ui.fragment.ArtistFragment
import com.newamber.ambermusic.mvp.ui.fragment.PlaybackMainFragment
import com.newamber.ambermusic.mvp.ui.fragment.SongFragment
import com.newamber.ambermusic.util.*
import isGone
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * Created by Newamber on 2018/4/7.
 */
class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(),
    MainContract.View,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener {

    @Inject
    override lateinit var presenter: MainContract.Presenter
    override var enableRxBus = true

    private val timer by lazy { Timer() }
    private val player by lazy { MediaPlayer() }
    private lateinit var songList: List<Song>
    private var song: Song = SongLoader.getSong(SharePreStorage.getLastPlayedSongId())
    private var needInitSong = true

    //private val playManager by lazy { PlaybackManager(this) }

    override fun onDestroy() {
        super.onDestroy()
        // save last song
        SharePreStorage.saveLastPlayedSongId(song.id)
        SharePreStorage.saveLastPlayedProgress(player.currentPosition)
        release()
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun getMenuId() = R.menu.menu_toolbar_main

    override fun processMenuItemClickEvent(itemId: Int?) {
        when (itemId) {
            R.id.toolbar_main_menu_search -> {
                //post("SongFragment_Action_Show_Bar", "test message")
            }
        }
    }

    override fun initView() {
        //playManager.startService()
        //playManager.bindService()
        SongLoader.getAllSongs().subscribe { songList = it }
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)
        if (aboveMarshmallow()) checkPermission()

        // setup Toolbar & DrawerLayout
        // toolbarMain(id resource name) is defined in activity_main.xml
        // and instantiated by relative kotlin libs, other Views are the same with it
        with(toolbarMain) {
            setSupportActionBar(this)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setHomeButtonEnabled(true)
            }
            title = getString(string.app_name)
            with(
                ActionBarDrawerToggle(
                    this@MainActivity,
                    drawerLayout,
                    this,
                    string.open,
                    string.close
                )
            ) {
                syncState()
                drawerLayout.addDrawerListener(this)
            }
        }

        // setup Navigation
        with(navigationView) {
            //itemIconTintList = null
            itemTextColor = findColorStateList(color.list_color_drawer)
            setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_menu_media_library -> {
                    }
                    R.id.navigation_menu_folder -> {
                    }
                    R.id.navigation_menu_settings -> {
                    }
                    R.id.navigation_menu_donation -> {
                    }
                    R.id.navigation_menu_about -> {
                    }
                }
                drawerLayout.closeDrawers()
                false
            }
        }

        // setup ViewPager
        with(viewPagerMain) {
            adapter = object : BaseFragmentPagerAdapter(
                supportFragmentManager,
                listOf(SongFragment(), AlbumFragment(), ArtistFragment())
            ) {}

            //disableShiftMode(bottomNavigationView)
            // synchronize bottomNavigationView with viewpager
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    bottomNavigationView.menu.getItem(position).isChecked = true
                }
            })
            bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.tabs_main_song -> this.currentItem = 0
                    R.id.tabs_main_album -> this.currentItem = 1
                    R.id.tabs_main_artist -> this.currentItem = 2
                //R.id.tabs_main_playlist -> this.currentItem = 3
                }
                false
            }
        }

        setupSlidingLayout()

        setupTabLayout()

        // setup last play state
        val progress = SharePreStorage.getLastPlayedProgress()
        logDebug(song)
        logDebug(progress)
        postSticky("event_init_last_play_state", arrayOf(song, progress))
        prepare()
    }

    // Contract.View method
    override fun toast(str: String) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_STORAGE_CODE -> if (!grantResults.isEmpty()
                && grantResults[0] == PackageManager.PERMISSION_DENIED
            ) {
                warningBar(
                    toolbarMain,
                    "Can not read media because you denied the relative permission!"
                )
            } else {
                finish() // TODO
            }
        }
    }


    override fun onPrepared(mp: MediaPlayer?) {
        if (!needInitSong)
            mp?.start()
        else
            player.seekTo(SharePreStorage.getLastPlayedProgress())
    }

    override fun onCompletion(mp: MediaPlayer?) = next()

    companion object {
        const val REQUEST_STORAGE_CODE = 0
        val storagePermissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    // -------------------------------------event method--------------------------------------------
    @Suppress("UNCHECKED_CAST")
    @Receive("event_play_song_main_activity")
    fun onEventPlaySongFromFragment(array: Array<Any>) {
        needInitSong = false
        song = array[0] as Song
        songList = array[1] as List<Song>
        prepare()
        post("event_update_ui_playback_fragment", song)
        updateTime()
    }

    @Receive("event_update_audio_progress")
    fun onEventSetSeekBar(progress: Int) {
        player.seekTo(progress)
    }

    @Receive("event_pause_main_activity")
    fun onEventPause() {
        needInitSong = false
        player.pause()
    }

    @Receive("event_play_main_activity")
    fun onEventPlay() {
        needInitSong = false
        player.start()
        updateTime()
    }

    @Receive("event_play_next_song_main_activity")
    fun next() {
        needInitSong = false
        val index = songList.indexOf(song)
        val nextSong = songList[(index + 1) % songList.size]
        song = nextSong
        prepare()
        post("event_update_ui_playback_fragment", song)
    }

    @Receive("event_play_pre_song_main_activity")
    fun previous() {
        needInitSong = false
        val index = songList.indexOf(song)
        val preSong = if (index == 0) {
            songList[songList.size - 1]
        } else {
            songList[index - 1]
        }
        song = preSong
        prepare()
        post("event_update_ui_playback_fragment", song)
    }

    // private method
    private fun setupTabLayout() {
        with(tabLayoutMain) {
            // this method removes tabs which already exit and
            // creates tabs according to FragmentPagerAdapter
            setupWithViewPager(viewPagerMain)
            val tabIcons = arrayOf(
                R.drawable.ic_tab_main_song,
                R.drawable.ic_tab_main_album,
                R.drawable.ic_tab_main_artist,
                R.drawable.ic_tab_main_playlist
            )

            repeat(tabCount) { getTabAt(it)?.setIcon(tabIcons[it]) }

            getTabAt(0)?.icon?.setColorFilter(
                findColor(color.colorTabSelectedIcon),
                PorterDuff.Mode.SRC_IN
            )

            // accent the color of any selected tab
            addOnTabSelectedListener(object : ViewPagerOnTabSelectedListener(viewPagerMain) {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    super.onTabSelected(tab)
                    tab?.icon?.setColorFilter(
                        findColor(color.colorTabSelectedIcon),
                        PorterDuff.Mode.SRC_IN
                    )
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.icon?.setColorFilter(
                        findColor(color.colorTabUnSelectedIcon),
                        PorterDuff.Mode.SRC_IN
                    )
                }
            })
            isGone = true
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        if (!isPermissionGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, storagePermissions[0])) {
                warningBar(
                    toolbarMain,
                    "Can not read media because you denied the relative permission!"
                )
            }
            ActivityCompat.requestPermissions(this, storagePermissions, REQUEST_STORAGE_CODE)
        }
    }

    private fun setupSlidingLayout() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.containerPlaybackMain, PlaybackMainFragment()).commit()
    }

    private fun prepare() {
        val path = SongLoader.getSongUri(song.id).toString()
        player.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)
        try {
            with(player) {
                reset()
                if (path.startsWith("content://")) {
                    setDataSource(this@MainActivity, path.toUri())
                } else {
                    setDataSource(path)
                }
                val attr = AudioAttributes
                    .Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                setAudioAttributes(attr)
                prepareAsync()
            }
        } catch (e: IOException) {
            logDebug(e)
        }
    }

    private fun release() {
        player.stop()
        player.release()
        timer.cancel()
    }

    private fun updateTime() = timer.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            post("event_update_progress", player.currentPosition)
        }
    }, 0L, 1000L)
}