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

@file:Suppress("unused")

package com.newamber.ambermusic.util

import android.util.Log
import com.newamber.ambermusic.constants.EMPTY_STRING
import com.orhanobut.logger.LogStrategy
import com.orhanobut.logger.Logger

// fix Logger UI bug on Android Studio 3.1
class LogFormatStrategy : LogStrategy {

    private var last = 0

    override fun log(priority: Int, tag: String?, message: String) {
        Log.println(priority, "${randomKey()}$tag", message)
    }

    private fun randomKey(): String {
        var random = (10 * Math.random()).toInt()
        if (random == last) {
            random = (random + 1) % 10
        }
        last = random
        return random.toString()
    }
}

inline fun <reified T> T.logVerbose(
    message: String = EMPTY_STRING,
    tag: String = "${T::class.simpleName}"
) = Logger.t(tag).v(message)

inline fun <reified T> T.logInfo(
    message: String = EMPTY_STRING,
    tag: String = "${T::class.simpleName}"
) = Logger.t(tag).i(message)

inline fun <reified T> T.logDebug(
    message: String = EMPTY_STRING,
    tag: String = "${T::class.simpleName}"
) = Logger.t(tag).d(message)

inline fun <reified T> T.logDebug(
    obj: Any,
    tag: String = "${T::class.simpleName}"
) = Logger.t(tag).d(obj)

inline fun <reified T> T.logWarn(
    message: String = EMPTY_STRING,
    tag: String = "${T::class.simpleName}"
) = Logger.t(tag).w(message)

inline fun <reified T> T.logError(
    message: String = EMPTY_STRING,
    tag: String = "${T::class.simpleName}"
) = Logger.t(tag).e(message)

inline fun <reified T> T.logAssert(
    message: String = EMPTY_STRING,
    tag: String = "${T::class.simpleName}"
) = Logger.t(tag).wtf(message)

inline fun <reified T> T.logJson(
    message: String = EMPTY_STRING,
    tag: String = "${T::class.simpleName}"
) = Logger.t(tag).json(message)