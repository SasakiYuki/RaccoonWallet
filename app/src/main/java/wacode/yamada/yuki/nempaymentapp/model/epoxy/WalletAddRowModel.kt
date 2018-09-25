package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.view.View
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletAddEntity

@EpoxyModelClass(layout = R.layout.row_wallet_add)
abstract class WalletAddRowModel : DataBindingEpoxyModel() {
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onClickRowListener: View.OnClickListener = View.OnClickListener { }

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var walletAddEntity: WalletAddEntity? = null

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.clickListener, onClickRowListener)
            it.setVariable(BR.entity, walletAddEntity)
        }
    }
}