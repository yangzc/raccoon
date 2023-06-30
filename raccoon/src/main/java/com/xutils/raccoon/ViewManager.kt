/**
 * Copyright (C) 2022 The AndroidSupport Project
 * Created by yangzc on 23/03/2022.
 */
package com.xutils.raccoon

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.Window
import java.lang.ref.WeakReference

object ViewManager {

    private var mViewList: ArrayList<WeakReference<View>> = ArrayList()

    fun onAttachedToWindow(viewRef: WeakReference<View>) {
        mViewList.add(viewRef)
    }

    fun onDetachedFromWindow(viewRef: WeakReference<View>) {
        mViewList.remove(viewRef)
    }

    /**
     * 获得所有关联到Windows的View
     */
    fun getAllAttachedViews(): ArrayList<View> {
        return getViewsByCondition {
            true
        }
    }

    /**
     * 根据位置获得所有关联到Windows的View
     */
    fun getAttachedViewsByXy(x: Int, y: Int): ArrayList<View> {
        var rect = Rect()
        return getViewsByCondition {
            var xy = IntArray(2)
            it.getLocationInWindow(xy)
            rect.set(xy[0], xy[1], xy[0] + it.width, xy[1] + it.height)
            rect.contains(x, y)
        }
    }

    /**
     * 根据条件获得关联到Windows的view
     */
    private fun getViewsByCondition(condition: (view: View) -> Boolean): ArrayList<View> {
        var result = ArrayList<View>()
        var iterator = mViewList.iterator()
        while (iterator.hasNext()) {
            var it = iterator.next()
            if (it.get() == null) {
                iterator.remove()
            } else {
                var view = it.get() as View
                if (condition(view)) {
                    result.add(view)
                }
            }
        }
        return result
    }

    /**
     * 获得最上层Layer
     */
    fun getTopLayerView(activity: Activity): ViewGroup {
        var topLayerView = activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        while (topLayerView != null) {
            topLayerView = getTopVisiblePanelInGroup(activity, topLayerView)
        }
        return topLayerView
    }

    /**
     * 在特定ViewGroup中获得满足条件的子节点
     */
    private fun getTopVisiblePanelInGroup(activity: Activity, viewGroup: ViewGroup): ViewGroup? {
        var metrics = activity.resources.displayMetrics
        for (i in viewGroup.childCount - 1 downTo 0) {
            val child = viewGroup.getChildAt(i)
            if (child.isShown && child is ViewGroup && child.width == metrics.widthPixels
                && child.height > metrics.heightPixels * 0.8f
            ) {
                return child
            }
        }
        return null
    }
}