/**
 * Copyright (C) 2022 The AndroidSupport Project
 * Created by yangzc on 24/03/2022.
 */
package com.xutils.raccoon.ext

import android.view.View
import android.view.ViewGroup

/**
 * 判断view是否在ViewGroup中
 */
fun ViewGroup.isChild(child: View): Boolean {
    var view: View = child
    if (child == this)
        return true
    while (view != null) {
        if (view.parent is View) {
            view = view.parent as View
            if (view == this) {
                return true
            }
        } else {
            return false
        }
    }
    return false
}