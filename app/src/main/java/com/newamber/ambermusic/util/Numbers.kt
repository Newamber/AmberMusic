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

import java.util.concurrent.TimeUnit

/**
 * Description: .
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/31.
 */
fun Int.toMinuteSecond(): String {
    val times = this.toLong()
    val res: String
    if (TimeUnit.MILLISECONDS.toHours(this.toLong()) > 1) {
        res = String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(times),
            TimeUnit.MILLISECONDS.toMinutes(times) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(times)
            ),
            TimeUnit.MILLISECONDS.toSeconds(times) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(times)
            )
        )
    } else {
        res = String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(times),
            TimeUnit.MILLISECONDS.toSeconds(times) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(times)
            )
        )
    }
    return res
}

