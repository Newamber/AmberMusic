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

package com.newamber.ambermusic.base

import android.support.annotation.ArrayRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.newamber.ambermusic.AmberMusic
import com.newamber.ambermusic.constants.EMPTY_STRING

/**
 * Description: A common v4.APP.fragment pager adapter for `ViewPager`.
 *
 * Created by Newamber on 2018/5/6.
 */
abstract class BaseFragmentPagerAdapter(
    manager: FragmentManager,
    private val fragmentList: List<Fragment>
) : FragmentPagerAdapter(manager) {

    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment? = fragmentList[position]

    override fun getPageTitle(position: Int): CharSequence? = EMPTY_STRING

}

/**
 * Description: A common v4.APP.fragment pager adapter for `ViewPager`
 * and `TabLayout` which can easily set title for each tabs.
 *
 * Created by Newamber on 2018/5/6.
 */
abstract class FragmentPagerTitleAdapter(
    manager: FragmentManager,
    fragmentList: List<Fragment>,
    @ArrayRes private val titleId: Int
) : BaseFragmentPagerAdapter(manager, fragmentList) {

    final override fun getPageTitle(position: Int): CharSequence? =
        AmberMusic.APP.resources.getStringArray(titleId)[position]

}