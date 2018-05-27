package wacode.yamada.yuki.nempaymentapp.view.custom_view

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_setting_list_item_normal_view.view.*
import wacode.yamada.yuki.nempaymentapp.R

class SettingSwitchItemView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {
    private lateinit var listener: OnSwitchClickListener
    private lateinit var switch: SwitchCompat

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    init {
        val view = View.inflate(context, R.layout.view_setting_switch_item, this)
        val attrs = context!!.obtainStyledAttributes(attrs, R.styleable.SettingSwitchItemView, defStyleAttr, 0)
        view.title.text = attrs.getString(R.styleable.SettingSwitchItemView_text)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        switch = findViewById(R.id.settingSwitch)
        switch.setOnClickListener {
            val isValid = switch.isChecked
            listener.onClick(isValid)
        }
    }

    fun setCheck(isCheck: Boolean) {
        switch.isChecked = isCheck
    }

    fun setOnSwitchClickListener(onSwitchClickListener: OnSwitchClickListener) {
        listener = onSwitchClickListener
    }

    interface OnSwitchClickListener {
        fun onClick(isCheck: Boolean)
    }
}