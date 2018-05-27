package wacode.yamada.yuki.nempaymentapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup

import wacode.yamada.yuki.nempaymentapp.R


class ViewPagerIndicator constructor(context: Context, attrs: AttributeSet? = null) : RadioGroup(context, attrs) {
    var indicatorResource = R.drawable.indicator
    private var mCount: Int = 0

    init {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
    }

    fun setCount(count: Int) {
        mCount = count
        removeAllViews()

        val context = context
        for (i in 0 until count) {
            val rb = RadioButton(context)
            rb.isFocusable = false
            rb.isClickable = false
            rb.setButtonDrawable(indicatorResource)
            addView(rb)
        }

        setCurrentPosition(-1)
    }

    fun setCurrentPosition(position: Int) {
        var position = position
        if (position >= mCount) {
            position = mCount - 1
        }
        if (position < 0) {
            position = if (mCount > 0) 0 else -1
        }

        if (position >= 0 && position < mCount) {
            val rb = getChildAt(position) as RadioButton
            rb.isChecked = true
        }
    }
}
