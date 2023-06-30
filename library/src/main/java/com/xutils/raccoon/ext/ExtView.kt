/**
 * Copyright (C) 2022 The AndroidSupport Project
 * Created by yangzc on 24/03/2022.
 */
package com.xutils.raccoon.ext

import android.graphics.Rect
import android.view.View
import kotlin.math.abs

fun View.isShowEnoughInUI(rect: Rect, showRate: Float): Boolean {
    return (abs(rect.bottom - rect.top) >= this.height * showRate
            && abs(rect.right - rect.left) >= this.width * showRate)
}