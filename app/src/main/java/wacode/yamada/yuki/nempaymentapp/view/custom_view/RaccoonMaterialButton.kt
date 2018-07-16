package wacode.yamada.yuki.nempaymentapp.view.custom_view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import wacode.yamada.yuki.nempaymentapp.R

class RaccoonMaterialButton(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : ConstraintLayout(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    init {
        View.inflate(context, R.layout.view_raccoon_material_button, this)
    }
}