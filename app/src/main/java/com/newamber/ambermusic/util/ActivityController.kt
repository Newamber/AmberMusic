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

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import com.newamber.ambermusic.AmberMusic
import java.util.*

/**
 * Description: .
 *
 * Created by Newamber on 2018/5/12.
 */
object ActivityController {

    private val stack by lazy { Stack<Activity>() }

    fun addCurrent(activity: Activity): Activity = stack.push(activity)

    fun removeCurrent() = stack.pop().finish()

    fun getCurrent() = stack.peek()

    @Suppress("MemberVisibilityCanBePrivate")
    fun finishAll() {
        stack.forEach { it.finish() }
        stack.clear()
    }

    fun exitApp() {
        finishAll()
        val manager = AmberMusic.APP.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.killBackgroundProcesses(AmberMusic.APP.packageName)
        System.exit(0)
    }
}