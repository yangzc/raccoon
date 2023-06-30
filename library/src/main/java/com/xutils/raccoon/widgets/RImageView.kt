/**
 * Copyright (C) 2022 The AndroidSupport Project
 * Created by yangzc on 23/03/2022.
 */
package com.xutils.raccoon.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TwoLineListItem
import androidx.appcompat.widget.AppCompatImageView
import com.xutils.raccoon.mixin.RView
import com.xutils.raccoon.mixin.RViewMixin

class RImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs),
    RView by RViewMixin(context, attrs) {

    override fun performClick(): Boolean {
        doClick(this)
        return super.performClick()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onAttachedToWindow(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDetachedFromWindow(this)
    }

    override fun onVisibilityAggregated(isVisible: Boolean) {
        super.onVisibilityAggregated(isVisible)
        onVisibilityAggregated(this, isVisible)
    }
}