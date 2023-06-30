/**
 * Copyright (C) 2022 The AndroidSupport Project
 * Created by yangzc on 23/03/2022.
 */
package com.xutils.raccoon.mixin

import android.content.Context
import android.graphics.Rect
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import com.xutils.raccoon.R
import com.xutils.raccoon.Raccoon
import com.xutils.raccoon.ViewManager
import com.xutils.raccoon.ext.isShowEnoughInUI
import java.lang.ref.WeakReference

/**
 * 核心作用
 * 1.收集View信息
 * 2.监控View状态
 */
class RViewMixin(context: Context, attrs: AttributeSet?) : RView,
    ViewTreeObserver.OnPreDrawListener, Runnable {

    private var mViewTag: String? = null
    private var mDataBundle: String? = null
    private var mViewRef: WeakReference<View>? = null

    private var mShowTs: Long = 0
    private var mFullShown = false
    private var mTempRect: Rect = Rect()

    init {
        var typeArray = context.obtainStyledAttributes(attrs, R.styleable.view_info)
        mViewTag = typeArray.getString(R.styleable.view_info_viewTag)
        mDataBundle = typeArray.getString(R.styleable.view_info_dataBundle)
        typeArray.recycle()
    }

    override fun doClick(view: View): Boolean {
        Raccoon.get().eventObserver?.let { observer ->
            mViewTag?.let {
                observer.onViewClick(view, it, mDataBundle)
            }
        }
        return Raccoon.get().isStopTheWorld()
    }

    override fun onAttachedToWindow(view: View) {
        mViewTag?.let {
            this.mViewRef = WeakReference(view)
            view.viewTreeObserver.addOnPreDrawListener(this)
        }
    }

    override fun onDetachedFromWindow(view: View) {
        mViewTag?.let {
            view.viewTreeObserver.removeOnPreDrawListener(this)
            updateUIStatus(view)
            this.mViewRef = null
        }
    }

    override fun onVisibilityAggregated(view: View, isVisible: Boolean) {
        mViewTag?.let {
            updateUIStatus(view)
        }
    }

    override fun onPreDraw(): Boolean {
        mViewRef?.get()?.let {
            updateUIStatus(it)
        }
        return true
    }

    override fun run() {
        mViewRef?.get()?.let {
            updateUIStatusImpl(it)
        }
    }

    private fun updateUIStatus(view: View) {
        view.removeCallbacks(this)
        view.postDelayed(this, 300)
    }

    private fun updateUIStatusImpl(view: View) {
        var isVisible = view.getGlobalVisibleRect(mTempRect) && view.isShown
        if (isVisible) {
            // 判定显示在系统中
            if (!mFullShown && view.isShowEnoughInUI(mTempRect, 1.0f)) {
                mShowTs = SystemClock.elapsedRealtime()
                mViewRef?.let { ViewManager.onAttachedToWindow(it) }
                Raccoon.get().eventObserver?.let { observer ->
                    mViewTag?.let {
                        observer.onViewFullShown(view, it, mDataBundle)
                    }
                }
                this.mFullShown = true
            }
        } else {
            // 判定为不显示
            if (mFullShown) {
                mViewRef?.let { ViewManager.onDetachedFromWindow(it) }
                Raccoon.get().eventObserver?.let { observer ->
                    mViewTag?.let {
                        observer.onViewFullHidden(
                            view,
                            it,
                            mDataBundle,
                            SystemClock.elapsedRealtime() - mShowTs
                        )
                    }
                }
                this.mFullShown = false
            }
        }
    }

}