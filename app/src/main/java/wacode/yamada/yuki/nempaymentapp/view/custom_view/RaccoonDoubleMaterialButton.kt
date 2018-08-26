package wacode.yamada.yuki.nempaymentapp.view.custom_view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.view_raccoon_double_material_button.view.*
import wacode.yamada.yuki.nempaymentapp.R

class RaccoonDoubleMaterialButton(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var onClickListener: OnMaterialButtonClickListener? = null

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    init {
        val view = View.inflate(context, R.layout.view_raccoon_double_material_button, this)
        context?.obtainStyledAttributes(attrs, R.styleable.RaccoonDoubleMaterialButton, defStyleAttr, 0)?.let {
            try {
                view.leftTextView.text = it.getString(R.styleable.RaccoonDoubleMaterialButton_leftText)
                val leftResourceId = it.getResourceId(R.styleable.RaccoonDoubleMaterialButton_leftSrc, R.mipmap.icon_harvest_small)
                view.leftImageView.setImageDrawable(ContextCompat.getDrawable(context, leftResourceId))
                view.rightTextView.text = it.getString(R.styleable.RaccoonDoubleMaterialButton_rightText)
                val rightResourceId = it.getResourceId(R.styleable.RaccoonDoubleMaterialButton_rightSrc, R.mipmap.icon_harvest_small)
                view.rightImageView.setImageDrawable(ContextCompat.getDrawable(context, rightResourceId))

                view.rightRootView.setOnClickListener {
                    onClickListener?.onRightClick()
                }

                view.leftRootView.setOnClickListener {
                    onClickListener?.onLeftClick()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                it.recycle()
            }
        }
    }

    fun setOnClickListener(listener: OnMaterialButtonClickListener) {
        onClickListener = listener
    }

    interface OnMaterialButtonClickListener {
        fun onRightClick()

        fun onLeftClick()
    }
}
