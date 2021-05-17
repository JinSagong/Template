package com.jin.template.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import com.jin.template.R
import kotlinx.android.synthetic.main.layout_bottom_slide_view.view.*

@Suppress("UNUSED")
class BottomSlideView : FrameLayout {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs)
    }

    private lateinit var llBg: LinearLayout
    private lateinit var flTop: FrameLayout
    private lateinit var ivBar: ImageView
    private lateinit var flContent: FrameLayout

    private var onSlide: OnSlide? = null

    private var maxHeight = 1000
    private var autoComplete = true

    private var bottomViewTouchHeight = 0f
    private var bottomViewTouchY = 0f

    val isCollapsed get() = (layoutParams as ViewGroup.LayoutParams).height == minimumHeight

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context, attrs: AttributeSet? = null) {
        val view = View.inflate(context, R.layout.layout_bottom_slide_view, null)
        view.layoutParams = ViewGroup.LayoutParams(-1, -1) // MATCH_PARENT

        llBg = view.ll_bottom_slide_view_bg
        flTop = view.fl_bottom_slide_view_top
        ivBar = view.iv_bottom_slide_view_bar
        flContent = view.fl_bottom_slide_view_content
        flTop.setOnTouchListener { _, event -> onTouchBar(event) }

        if (attrs != null) {
            val osa = context.obtainStyledAttributes(attrs, R.styleable.BottomSlideView)
            try {
                val bgResource =
                    osa.getResourceId(R.styleable.BottomSlideView_bottomSlideBackground, 0)
                llBg.setBackgroundResource(bgResource)
                val bgElevation =
                    osa.getDimension(R.styleable.BottomSlideView_bottomSlideBgElevation, 0f)
                llBg.elevation = bgElevation
                val barResource = osa.getResourceId(R.styleable.BottomSlideView_bottomSlideBar, 0)
                ivBar.setImageResource(barResource)
                val slideBarHeight =
                    osa.getDimensionPixelSize(R.styleable.BottomSlideView_bottomSlideBarHeight, 0)
                if (slideBarHeight != 0)
                    flTop.updateLayoutParams<LinearLayout.LayoutParams> { height = slideBarHeight }
                val mMaxHeight =
                    osa.getDimensionPixelSize(
                        R.styleable.BottomSlideView_bottomSlideMaxHeight, maxHeight
                    )
                maxHeight = mMaxHeight
                val mMinHeight =
                    osa.getDimensionPixelSize(R.styleable.BottomSlideView_bottomSlideMinHeight, 0)
                minimumHeight = mMinHeight
                layoutParams?.height = mMinHeight

                autoComplete =
                    osa.getBoolean(
                        R.styleable.BottomSlideView_bottomSlideAutoComplete, autoComplete
                    )
                val draggable =
                    osa.getBoolean(R.styleable.BottomSlideView_bottomSlideDraggable, true)
                flTop.visibility = if (draggable) View.VISIBLE else View.GONE
            } finally {
                osa.recycle()
            }
        }

        addView(view)
    }

    interface OnSlide {
        fun onSlideStart(isExtend: Boolean)
        fun onSliding(percent: Float)
        fun onSlideEnd(isExtend: Boolean)
    }

    private fun onTouchBar(event: MotionEvent): Boolean {
        when {
            MotionEvent.ACTION_DOWN == event.action -> {
                bottomViewTouchHeight = measuredHeight.toFloat()
                bottomViewTouchY = event.rawY
                val doHeight = (bottomViewTouchHeight + (bottomViewTouchY - event.rawY)).toInt()
                onSlide?.onSlideStart(getHalfLine() < doHeight)
            }
            MotionEvent.ACTION_UP == event.action -> {
                val doHeight = (bottomViewTouchHeight + (bottomViewTouchY - event.rawY)).toInt()
                if (doHeight >= maxHeight || doHeight <= minimumHeight) {
                    if (onSlide != null) {
                        if (doHeight <= minimumHeight) onSlide!!.onSliding(getPercent(minimumHeight.toFloat()))
                        else if (doHeight >= maxHeight) onSlide!!.onSliding(getPercent(maxHeight.toFloat()))
                        onSlide!!.onSlideEnd(getHalfLine() < doHeight)
                    }
                    return true
                }
                val e =
                    if (getHalfLine() < doHeight) maxHeight.toFloat() else minimumHeight.toFloat()
                if (autoComplete) autoSlideAni(e, false)
            }
            MotionEvent.ACTION_MOVE == event.action -> {
                var doHeight = (bottomViewTouchHeight + (bottomViewTouchY - event.rawY)).toInt()
                if (doHeight > maxHeight) {
                    doHeight = maxHeight
                } else if (doHeight < minimumHeight) doHeight = minimumHeight else {
                    if (onSlide != null) onSlide!!.onSliding(getPercent(doHeight.toFloat()))
                }
                (layoutParams as ViewGroup.LayoutParams).height = doHeight
                requestLayout()
            }
        }
        return true
    }

    private fun autoSlideAni(e: Float, runStartListener: Boolean) {
        val s = (layoutParams as ViewGroup.LayoutParams).height.toFloat()
        val tranY = ValueAnimator.ofFloat(s, e).setDuration(400L)
        tranY.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            (layoutParams as ViewGroup.LayoutParams).height = value.toInt()
            requestLayout()
            onSlide?.onSliding(getPercent(value))

        }
        tranY.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator, isReverse: Boolean) {
                if (runStartListener) onSlide?.onSlideStart(false)
            }

            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                onSlide?.onSlideEnd(getHalfLine() < (layoutParams as ViewGroup.LayoutParams).height)
            }
        })
        tranY.interpolator = AccelerateDecelerateInterpolator()
        tranY.start()
    }

    private fun getPercent(thisValue: Float): Float {
        val b = maxHeight.toFloat() - minimumHeight.toFloat()
        val s = thisValue - minimumHeight.toFloat()
        return s / b * 100f
    }

    private fun getHalfLine() = (maxHeight + minimumHeight) / 2

    fun setOnSlide(onSlide: OnSlide) = apply { this.onSlide = onSlide }

    fun setMaxHeight(height: Int) = apply { maxHeight = height }

    fun setMinHeight(height: Int) = apply {
        minimumHeight = height
        layoutParams.height = height
    }

    fun setBarHeight(height: Int) = apply {
        flTop.updateLayoutParams<LinearLayout.LayoutParams> { this.height = height }
    }

    fun setBgResource(resourceId: Int) = apply { llBg.setBackgroundResource(resourceId) }
    fun setBarResource(resourceId: Int) = apply { ivBar.setImageResource(resourceId) }

    fun setView(v: View) = apply {
        flContent.removeAllViews()
        flContent.addView(v)
    }

    fun setAutoComplete(autoComplete: Boolean) = apply { this.autoComplete = autoComplete }

    fun setDraggable(draggable: Boolean) = apply {
        flTop.visibility = if (draggable) View.VISIBLE else View.GONE
    }

    fun expand() = post { autoSlideAni(maxHeight.toFloat(), true) }
    fun collapse() = post { autoSlideAni(minimumHeight.toFloat(), true) }
}