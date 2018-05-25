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

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Environment
import android.os.Process
import android.widget.Toast
import com.newamber.ambermusic.AmberMusic
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")

/**
 * Description: Refer to the code of *Ren Yugang(任玉刚)*.
 *
 * Created by Newamber on 2018/5/11.
 */
object CrashHandler : Thread.UncaughtExceptionHandler {

    private val defaultCrashHandler: Thread.UncaughtExceptionHandler? by lazy {
        Thread.getDefaultUncaughtExceptionHandler()
    }

    private val path = "${Environment.getExternalStorageDirectory().path}/crash/log/"

    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        dumpExceptionToSDCard(e)
        e?.printStackTrace()
        if (defaultCrashHandler != null) {
            defaultCrashHandler?.uncaughtException(t, e)
        } else {
            Toast.makeText(AmberMusic.APP, "Crash info dumped into sdcard.", Toast.LENGTH_LONG)
                .show()
            Process.killProcess(Process.myPid())
            ActivityController.exitApp()
        }
    }

    private fun dumpExceptionToSDCard(e: Throwable?) {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            logWarn("sdcard unmounted, skip dump.")
            return
        }
        val dir = File(path)
        if (!dir.exists()) dir.mkdir()
        val time = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date(System.currentTimeMillis()))
        val logFile = File("${path}crash-info-$time.trace")
        PrintWriter(BufferedWriter(FileWriter(logFile))).use {
            println("Amber Music Crash Info ———— [$time]")
            val manager = AmberMusic.APP.packageManager
            val info = manager.getPackageInfo(
                AmberMusic.APP.packageName, PackageManager.GET_ACTIVITIES
            )
            println("App Version: ${info.versionName}-${info.versionCode}")
            println("OS Version: ${android.os.Build.VERSION.RELEASE}-${android.os.Build.VERSION.SDK_INT}")
            println("Vendor: ${android.os.Build.MANUFACTURER}")
            println("Model: ${android.os.Build.MODEL}")
            println("CPU ABI: ${android.os.Build.SUPPORTED_ABIS}")
            println()
            e?.printStackTrace()
        }
    }
}