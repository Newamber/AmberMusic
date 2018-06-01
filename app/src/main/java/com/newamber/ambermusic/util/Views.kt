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

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.support.annotation.AnimatorRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.CardView
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.newamber.ambermusic.AmberMusic
import com.newamber.ambermusic.constants.GENERAL_ANIMATION_DURATION
import com.newamber.ambermusic.util.findColor
import com.newamber.ambermusic.util.logDebug

fun View.startAnimator(@AnimatorRes id: Int, delay: Long = 0) {
    with(AnimatorInflater.loadAnimator(AmberMusic.APP, id)) {
        startDelay = delay
        setTarget(this)
        start()
    }
}

/**
 * A copy of `View.kt` from library android-ktx
 */
inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

/**
 * A copy of `View.kt` from library android-ktx
 */
inline var View.isInvisible: Boolean
    get() = visibility == View.INVISIBLE
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

/**
 * A copy of `View.kt` from library android-ktx
 */
inline var View.isGone: Boolean
    get() = visibility == View.GONE
    set(value) {
        visibility = if (value) View.GONE else View.VISIBLE
    }

/**
 * Set a gradual background color for this view.
 *
 * @param colorFromId begin color
 * @param colorToId end color
 * @param duration the length of the animation, in milliseconds. This value cannot
 * be negative.
 * @param delay the amount of the delay, in milliseconds
 * @param interpolator the interpolator to be used by this animation.
 *
 * @author Newamber
 */
fun View.makeGradualColor(
    @ColorRes colorFromId: Int,
    @ColorRes colorToId: Int,
    duration: Long = GENERAL_ANIMATION_DURATION,
    delay: Long = 0,
    interpolator: Interpolator = LinearInterpolator()
) {
    with(ValueAnimator.ofArgb(findColor(colorFromId), findColor(colorToId))) {
        setDuration(duration)
        this@with.interpolator = interpolator
        startDelay = delay
        start()
        addUpdateListener {
            val view = this@makeGradualColor
            val color = it.animatedValue as Int
            if (view is CardView) {
                view.setCardBackgroundColor(color)
            } else {
                view.setBackgroundColor(color)
            }
        }
    }
}

fun View.makeGradualColor(
    @ColorInt colorTo: Int,
    duration: Long = GENERAL_ANIMATION_DURATION,
    delay: Long = 0L,
    interpolator: Interpolator = LinearInterpolator()
) {
    with(ValueAnimator.ofArgb((background as ColorDrawable).color, colorTo)) {
        setDuration(duration)
        this@with.interpolator = interpolator
        startDelay = delay
        start()
        addUpdateListener {
            val view = this@makeGradualColor
            val color = it.animatedValue as Int
            if (view is CardView) {
                view.setCardBackgroundColor(color)
            } else {
                view.setBackgroundColor(color)
            }
        }
    }
}

/**
 * The same usage with [View.makeGradualColor].
 *
 * @author Newamber
 */
fun TextView.makeGradualTextColor(
    @ColorRes colorFromId: Int,
    @ColorRes colorToId: Int,
    duration: Long = GENERAL_ANIMATION_DURATION,
    delay: Long = 0L,
    interpolator: Interpolator = LinearInterpolator()
) {
    with(ValueAnimator.ofArgb(findColor(colorFromId), findColor(colorToId))) {
        setDuration(duration)
        this@with.interpolator = interpolator
        startDelay = delay
        start()
        addUpdateListener {
            this@makeGradualTextColor.setTextColor(it.animatedValue as Int)
        }
    }
}

fun TextView.makeGradualTextColor(
    @ColorRes colorToId: Int,
    duration: Long = GENERAL_ANIMATION_DURATION,
    delay: Long = 0L,
    interpolator: Interpolator = LinearInterpolator()
) {
    with(ValueAnimator.ofArgb((background as ColorDrawable).color, findColor(colorToId))) {
        setDuration(duration)
        this@with.interpolator = interpolator
        startDelay = delay
        start()
        addUpdateListener {
            this@makeGradualTextColor.setTextColor(it.animatedValue as Int)
        }
    }
}

//fun ImageView.toBitmap(): Bitmap {
//    isDrawingCacheEnabled = true
//    buildDrawingCache(true)
//    return drawingCache
//}


@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val bottomView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = bottomView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(bottomView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until bottomView.childCount) {
            val item = bottomView.getChildAt(i) as BottomNavigationItemView
            item.setShiftingMode(false)
            // set once again checked value, so view will be updated
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        logDebug(e)
    } catch (e: IllegalAccessException) {
        logDebug(e)
    }

}

/**
 * Start animators from animator resource and assign to specified views.
 *
 * @param animId animator id resource
 * @param views specified views apply the same animator
 * @param delay animator start delay(mills)
 *
 * @author Newamber
 */
fun startAnimators(@AnimatorRes animId: Int, vararg views: View, delay: Long = 0) {
    views.forEach { it.startAnimator(animId, delay) }
}

//fun ImageView.load(@DrawableRes drawableId: Int, activity: Activity) {
//    Glide.with(activity).load(drawableId).into(this)
//}