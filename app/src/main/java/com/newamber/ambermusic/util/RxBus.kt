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

import com.lsxiao.apollo.core.Apollo

/**
 * Used to post a message to specified method with `@Receive(tag)` annotation
 * and then you can do something in that method.
 *
 * e.g.
 * ```
 * post("SOMEACTIVITY_ACTION_DO_SOMETHING")
 *
 * ...
 *
 * @Receive("SOMEACTIVITY_ACTION_DO_SOMETHING")
 * fun onEventDoSomething() {
 *     ....
 * }
 * ```
 *
 * Created by Newamber(newamber@live.cn) on 2018/5/21.
 */
fun post(tag: String) = Apollo.emit(tag)

/**
 * @see [post]
 */
fun post(tag: String, obj: Any) = Apollo.emit(tag, obj)


/**
 * @see [post]
 */
fun postSticky(tag: String) = Apollo.emit(tag, true)


/**
 * @see [post]
 */
fun postSticky(tag: String, obj: Any) = Apollo.emit(tag, obj, true)

