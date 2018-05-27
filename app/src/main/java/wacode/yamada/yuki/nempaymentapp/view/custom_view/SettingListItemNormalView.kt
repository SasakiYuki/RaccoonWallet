package wacode.yamada.yuki.nempaymentapp.view.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_setting_list_item_normal_view.view.*
import wacode.yamada.yuki.nempaymentapp.R


class SettingListItemNormalView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    init {
        val view = View.inflate(context, R.layout.view_setting_list_item_normal_view, this)
        val attrs = context!!.obtainStyledAttributes(attrs, R.styleable.SettingListItemNormalView, defStyleAttr, 0)
        view.title.text = attrs.getString(R.styleable.SettingListItemNormalView_text)
    }
}