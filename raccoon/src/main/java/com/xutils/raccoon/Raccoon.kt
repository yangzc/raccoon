/**
 * Copyright (C) 2022 The AndroidSupport Project
 * Created by yangzc on 23/03/2022.
 */
package com.xutils.raccoon

import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.LayoutInflaterCompat
import com.xutils.raccoon.widgets.*

/**
 * Raccoon 小浣熊
 */
class Raccoon private constructor() : LayoutInflater.Factory2 {

    private val mMockViewMap = HashMap<String, Class<out Any>>()
    var eventObserver: ViewEventObserver? = null

    @Volatile private var mStopTheWorld: Boolean = false

    fun setStopTheWorld(stopTheWorld: Boolean) {
        Looper.myLooper()?.let {
            this.mStopTheWorld = stopTheWorld
            if (mStopTheWorld) {// 如果stopTheWorld则1秒之后还原世界
                android.os.Handler(it).postDelayed({
                    mStopTheWorld = false
                }, 1000)
            }
        }
    }

    fun isStopTheWorld(): Boolean {
        return mStopTheWorld
    }

    init {
        mMockViewMap["TwoLineListItem"] = RTwoLineListItem::class.java
        mMockViewMap["LinearLayout"] = RLinearLayout::class.java
        mMockViewMap["RelativeLayout"] = RRelativeLayout::class.java
        mMockViewMap["FrameLayout"] = RFrameLayout::class.java
        mMockViewMap["androidx.constraintlayout.widget.ConstraintLayout"] =
            RConstraintLayout::class.java
        mMockViewMap["androidx.viewpager.widget.ViewPager"] = RViewPager::class.java
        mMockViewMap["ImageView"] = RImageView::class.java
        mMockViewMap["TextView"] = RTextView::class.java
        mMockViewMap["androidx.recyclerview.widget.RecyclerView"] = RRecyclerView::class.java
        mMockViewMap["ImageButton"] = RImageButton::class.java
    }

    fun install(context: Context) {
        LayoutInflaterCompat.setFactory2(LayoutInflater.from(context), this)
    }

    fun putMockView(name: String, cls: Class<out Any>) {
        mMockViewMap[name] = cls
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return buildView(name, context, attrs)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return buildView(name, context, attrs)
    }

    private fun buildView(
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        var kClass = mMockViewMap[name]
        if (kClass != null) {
            var constructor = kClass.getConstructor(Context::class.java, AttributeSet::class.java)
            return constructor.newInstance(context, attrs) as View?
        }
        return null
    }

    companion object {
        private var instance: Raccoon? = null
            get() {
                if (field == null)
                    field = Raccoon()
                return field
            }

        fun get(): Raccoon {
            return instance!!
        }
    }

}