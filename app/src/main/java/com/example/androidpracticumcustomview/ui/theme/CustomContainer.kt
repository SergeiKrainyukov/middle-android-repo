package com.example.androidpracticumcustomview.ui.theme

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */

class CustomContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            val childLeft = (width - childWidth) / 2
            val childTop = (height - childHeight) / 2

            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
        }
    }

    override fun addView(child: View) {
        if (childCount < 2) {
            super.addView(child)
            animateView(child)
        } else
            throw IllegalStateException("You can't add more than two views")
    }

    private fun animateView(view: View) {
        val height = getScreenHeight()
        val startHeight = if (childCount == 1) -height / 2.3f else height / 2.3f

        ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
            interpolator = AccelerateInterpolator()
            duration = DEFAULT_ALPHA_DURATION_MILLIS.toLong()
            start()
        }

        ObjectAnimator.ofFloat(view, "translationY", 0f, startHeight).apply {
            interpolator = DecelerateInterpolator()
            duration = DEFAULT_ANIMATION_DURATION_MILLIS.toLong()
            start()
        }
    }

    private fun getScreenHeight(): Int {
        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.height()
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }
}