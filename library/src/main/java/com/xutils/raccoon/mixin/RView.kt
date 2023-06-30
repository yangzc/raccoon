/**
 * Copyright (C) 2022 The AndroidSupport Project
 * Created by yangzc on 23/03/2022.
 */
package com.xutils.raccoon.mixin

import android.view.View

interface RView {

    /**
     * 返回知否拦截点击事件
     */
    fun doClick(view: View): Boolean
    fun onAttachedToWindow(view: View)
    fun onDetachedFromWindow(view: View)
    fun onVisibilityAggregated(view: View, isVisible: Boolean)
}