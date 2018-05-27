package wacode.yamada.yuki.nempaymentapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.ryuta46.nemkotlin.model.HarvestInfo
import kotlinx.android.synthetic.main.view_mini_harvest_item.view.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.utils.HarvestAppConverter

class MiniHarvestItemView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null, 0)

    init {
        View.inflate(context, R.layout.view_mini_harvest_item, this)
    }

    fun setupHarvest(harvestInfo: HarvestInfo) {
        val harvestItem = HarvestAppConverter.convert(harvestInfo)
        dateTimeTextView.text = harvestItem.timeString

        amountTextView.text = (harvestItem.totalFee + " " + context.getString(R.string.com_xem_uppercase))
    }
}
