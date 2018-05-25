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

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import com.newamber.ambermusic.R
import com.newamber.ambermusic.constants.SNACK_BAR_DEFAULT_ACTION_TEXT
import io.github.tonnyl.light.*

fun normalBar(
    rootView: View,
    text: CharSequence,
    actionText: CharSequence = SNACK_BAR_DEFAULT_ACTION_TEXT,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (View) -> Unit = { }
) = normal(rootView, text, duration).setAction(actionText, listener).show()

fun normalBar(
    rootView: View,
    @StringRes textId: Int,
    @StringRes actionTextId: Int = R.string.snack_bar_default_action_text,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (View) -> Unit = { }
) = normal(rootView, textId, duration).setAction(actionTextId, listener).show()

fun infoBar(
    rootView: View,
    text: CharSequence,
    actionText: CharSequence = SNACK_BAR_DEFAULT_ACTION_TEXT,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (View) -> Unit = { }
) = info(rootView, text, duration).setAction(actionText, listener).show()

fun infoBar(
    rootView: View,
    @StringRes textId: Int,
    @StringRes actionTextId: Int = R.string.snack_bar_default_action_text,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (View) -> Unit = { }
) = info(rootView, textId, duration).setAction(actionTextId, listener).show()

fun successBar(
    rootView: View,
    text: CharSequence,
    actionText: CharSequence = SNACK_BAR_DEFAULT_ACTION_TEXT,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (View) -> Unit = { }
) = success(rootView, text, duration).setAction(actionText, listener).show()

fun successBar(
    rootView: View,
    @StringRes textId: Int,
    @StringRes actionTextId: Int = R.string.snack_bar_default_action_text,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (View) -> Unit = { }
) = success(rootView, textId, duration).setAction(actionTextId, listener).show()

fun warningBar(
    rootView: View,
    text: CharSequence,
    actionText: CharSequence = SNACK_BAR_DEFAULT_ACTION_TEXT,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (View) -> Unit = { }
) = warning(rootView, text, duration).setAction(actionText, listener).show()

fun warningBar(
    rootView: View,
    @StringRes textId: Int,
    @StringRes actionTextId: Int = R.string.snack_bar_default_action_text,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (View) -> Unit = { }
) = warning(rootView, textId, duration).setAction(actionTextId, listener).show()

fun errorBar(
    rootView: View,
    text: CharSequence,
    actionText: CharSequence = SNACK_BAR_DEFAULT_ACTION_TEXT,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (View) -> Unit = { }
) = error(rootView, text, duration).setAction(actionText, listener).show()

fun errorBar(
    rootView: View,
    @StringRes textId: Int,
    @StringRes actionTextId: Int = R.string.snack_bar_default_action_text,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (View) -> Unit = { }
) = error(rootView, textId, duration).setAction(actionTextId, listener).show()

fun developingBar(rootView: View) = infoBar(rootView, R.string.developing)