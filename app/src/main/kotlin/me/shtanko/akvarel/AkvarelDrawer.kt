package me.shtanko.akvarel.installed

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.animation.addListener
import me.shtanko.core.utils.AndroidUtils

class AkvarelDrawer @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val MIN_DRAWER_MARGIN = 64

    private var velocityTracker: VelocityTracker? = null
    private var startedTracking: Boolean = false
    private var maybeStartTracking: Boolean = false
    private var startedTrackingPointerId: Int = 0
    private var startedTrackingX: Int = 0
    private var startedTrackingY: Int = 0
    private var drawerPosition: Float = 0.toFloat()
    private var drawerOpened: Boolean = false
    private var beginTrackingSent: Boolean = false
    private lateinit var drawerLayout: ViewGroup
    private var scrimOpacity: Float = 0f
    private lateinit var lastInsets: WindowInsets
    private var inLayout: Boolean = false
    private var currentAnimation: AnimatorSet? = null
    private var allowDrawContent = true
    private val scrimPaint = Paint()
    private var minDrawerMargin = (MIN_DRAWER_MARGIN * AndroidUtils.density + 0.5f).toInt()

    private val LOG_TAG = "SHTANKO_CUSTOM_DRAWER"

    init {

        isFocusableInTouchMode = true
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS

        fitsSystemWindows = true
        setOnApplyWindowInsetsListener { view, windowInsets ->

            val drawerLayout = view as AkvarelDrawer
            lastInsets = windowInsets
            drawerLayout.setWillNotDraw(windowInsets.systemWindowInsetTop <= 0 && background == null)
            drawerLayout.refreshLayout()

            return@setOnApplyWindowInsetsListener windowInsets.consumeSystemWindowInsets()

        }

        systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    }

    fun refreshLayout() {

    }

    fun setDrawerLayout(layout: ViewGroup) {
        drawerLayout = layout
        addView(drawerLayout)
        if (Build.VERSION.SDK_INT >= 21) {
            drawerLayout.fitsSystemWindows = true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (drawerOpened && event != null && event.x > drawerPosition && !startedTracking) {
            return true
        }

        Log.d(LOG_TAG, "onTouchEvent: $event")

        if (event != null && (event.action == ACTION_MOVE || event.action == ACTION_DOWN) && !startedTracking && !maybeStartTracking) {
            Log.d(LOG_TAG, "onTouchEvent: 1")
            startedTrackingPointerId = event.getPointerId(0)
            maybeStartTracking = true
            startedTrackingX = event.x.toInt()
            startedTrackingY = event.y.toInt()
            if (velocityTracker != null) {
                velocityTracker?.clear()
            }

        } else if (event != null && event.action == MotionEvent.ACTION_MOVE && event.getPointerId(
                        0
                ) == startedTrackingPointerId
        ) {
            Log.d(LOG_TAG, "onTouchEvent: 2")
            if (velocityTracker == null) {
                velocityTracker = VelocityTracker.obtain()
            }

            val dx = ((event.x - startedTrackingX).toInt()).toFloat()
            val dy = Math.abs(event.y.toInt() - startedTrackingY)
            velocityTracker?.addMovement(event)

            if (maybeStartTracking &&
                    !startedTracking &&
                    (dx > 0 && dx / 3.0f > Math.abs(dy) &&
                            Math.abs(dx) >= AndroidUtils.getPixelsInCM(0.2f, true) || dx < 0 && Math.abs(
                            dx
                    ) >= Math.abs(dy) &&
                            Math.abs(dx) >= AndroidUtils.getPixelsInCM(0.4f, true))
            ) {

                prepareForDrawerOpen(event)
                startedTrackingX = event.x.toInt()
                requestDisallowInterceptTouchEvent(true)

            } else if (startedTracking) {
                if (!beginTrackingSent) {
                    beginTrackingSent = true
                }

                moveDrawerByX(dx)
                startedTrackingX = event.x.toInt()

            }

        } else if (event == null && event?.getPointerId(
                        0
                ) == startedTrackingPointerId && (event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_POINTER_UP)
        ) {
            Log.d(LOG_TAG, "onTouchEvent: 3")

            startedTracking = false
            maybeStartTracking = false
        }

        //return startedTracking
        return true

    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        if (maybeStartTracking && !startedTracking) {
            onTouchEvent(null)
        }

        super.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    private fun moveDrawerByX(dx: Float) {
        setDrawerPosition(drawerPosition + dx)
        Log.d(LOG_TAG, "moveDrawerByX".toUpperCase())
    }

    private fun setDrawerPosition(value: Float) {
        drawerPosition = value
        if (drawerPosition > drawerLayout.measuredWidth) {
            drawerPosition = drawerLayout.measuredWidth.toFloat()
        } else if (drawerPosition < 0) {
            drawerPosition = 0f
        }
        drawerLayout.translationX = drawerPosition
        val newVisibility = if (drawerPosition > 0) View.VISIBLE else GONE
        if (drawerLayout.visibility != newVisibility) {
            drawerLayout.visibility = newVisibility
        }
        setScrimOpacity(drawerPosition / drawerLayout.measuredWidth)
    }

    private fun setScrimOpacity(value: Float) {
        scrimOpacity = value
        invalidate()
    }

    private fun prepareForDrawerOpen(event: MotionEvent?) {
        maybeStartTracking = false
        startedTracking = true
        if (event != null) {
            startedTrackingX = event.x.toInt()
        }
        beginTrackingSent = false
    }

    override fun onMeasure(
            widthMeasureSpec: Int,
            heightMeasureSpec: Int
    ) {

        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(widthSize, heightSize)

//    val applyInsets = lastInsets != null && Build.VERSION.SDK_INT >= 21

        val childCount = childCount

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            val lp = child.layoutParams as MarginLayoutParams

            if (drawerLayout != child) {
                val contentWidthSpec = MeasureSpec.makeMeasureSpec(
                        widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY
                )
                val contentHeightSpec = MeasureSpec.makeMeasureSpec(
                        heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY
                )
                child.measure(contentWidthSpec, contentHeightSpec)
            } else {
                child.setPadding(0, 0, 0, 0);
                val drawerWidthSpec = getChildMeasureSpec(
                        widthMeasureSpec, minDrawerMargin + lp.leftMargin + lp.rightMargin, lp.width
                );
                val drawerHeightSpec =
                        getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height);
                child.measure(drawerWidthSpec, drawerHeightSpec);
            }

        }


        Log.d(LOG_TAG, "onMeasure: $widthSize $heightSize")

    }

    private fun cancelCurrentAnimation() {
        if (currentAnimation != null) {
            currentAnimation?.cancel()
            currentAnimation = null
        }
    }

    @SuppressLint("AnimatorKeep")
    fun openDrawer() {
        cancelCurrentAnimation()
        val animatorSet = AnimatorSet()

        val objectAnimator =
                ObjectAnimator.ofFloat(this, "drawerPosition", drawerLayout.measuredWidth.toFloat())
        animatorSet.playTogether(objectAnimator)
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.duration = 300

        animatorSet.addListener {
            onDrawerAnimationEnd(true)
        }

        animatorSet.start()

        currentAnimation = animatorSet
    }

    private fun onDrawerAnimationEnd(opened: Boolean) {
        startedTracking = false
        currentAnimation = null
        drawerOpened = opened
        if (!opened) {

        }
    }

    override fun onLayout(
            changed: Boolean,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
    ) {
        Log.d(LOG_TAG, "onLayout: $changed $left $top $right $bottom")

        inLayout = true
        val count = childCount
        Log.d(LOG_TAG, "onLayout child count: $count")
        for (i in 0 until count) {
            val child = getChildAt(i)

            Log.d(LOG_TAG, "onLayout child: $child")
            val lp = child.layoutParams as MarginLayoutParams

            if (drawerLayout != child) {
                child.layout(
                        lp.leftMargin, lp.topMargin + paddingTop, lp.leftMargin + child.measuredWidth,
                        lp.topMargin + child.measuredHeight + paddingTop
                )
            } else {
                child.layout(
                        -child.measuredWidth, lp.topMargin + paddingTop, 0,
                        lp.topMargin + child.measuredHeight + paddingTop
                )
            }

        }

        inLayout = false
//    super.onLayout(changed, left, top, right, bottom)
    }

    override fun drawChild(
            canvas: Canvas?,
            child: View?,
            drawingTime: Long
    ): Boolean {
        Log.d(LOG_TAG, "drawChild: $canvas $child $drawingTime")

        if (!allowDrawContent) return false

        val height = height
        val drawingContent = child != drawerLayout
        var lastVisibleChild: Int = 0
        var clipLeft: Int = 0
        val clipRight: Int = width

        val restoreCount = canvas?.save()

        if (drawingContent) {
            val childCount = childCount
            for (i in 0 until childCount) {
                val view = getChildAt(i)
                if (view.visibility == View.VISIBLE && view != drawerLayout) {

                }

                val vRight = view.right
                if (vRight > clipLeft) {
                    clipLeft = vRight
                }

            }

            if (clipLeft != 0) {
                canvas?.clipRect(clipLeft, 0, clipRight, height)
            }
        }

        val result = super.drawChild(canvas, child, drawingTime)
        restoreCount?.let {
            canvas.restoreToCount(it)
        }

        if (scrimOpacity > 0 && drawingContent) {
            if (indexOfChild(child) == lastVisibleChild) {
                //scrimPaint.color = ((((0x99000000.toInt() & 0xff000000) >>> 24) * scrimOpacity) << 24)
                canvas?.drawRect(clipLeft.toFloat(), 0f, clipRight.toFloat(), height.toFloat(), scrimPaint)
            }
        }

        return result

    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

}