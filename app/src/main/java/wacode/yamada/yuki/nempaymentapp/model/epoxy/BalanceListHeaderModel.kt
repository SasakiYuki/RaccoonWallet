package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicItem


@EpoxyModelClass(layout = R.layout.row_balance_list_header)
abstract class BalanceListHeaderModel : DataBindingEpoxyModel() {
    @EpoxyAttribute
    var mosaicItem: MosaicItem? = null

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.mosaic, mosaicItem)
        }
    }
}