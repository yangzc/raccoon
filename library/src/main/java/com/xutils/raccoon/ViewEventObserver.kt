/**
 * Copyright (C) 2022 The AndroidSupport Project
 * Created by yangzc on 23/03/2022.
 */
package com.xutils.raccoon

import android.view.View

interface ViewEventObserver {

    fun onViewClick(view: View, viewTag: String, dataBundle: String?)

    fun onViewFullShown(view: View, viewTag: String, dataBundle: String?)

    fun onViewFullHidden(view: View, viewTag: String, dataBundle: String?, duration: Long)
}