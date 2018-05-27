package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.item.SendMosaicItem

@EpoxyModelClass(layout = R.layout.row_send_confirm_main)
abstract class SendConfirmMainRowModel : DataBindingEpoxyModel() {
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var sendMosaicItem: SendMosaicItem? = null

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.sendMosaicItem, sendMosaicItem)
        }
    }
}
