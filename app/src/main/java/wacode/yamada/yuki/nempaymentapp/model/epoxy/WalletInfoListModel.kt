package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

@EpoxyModelClass(layout = R.layout.row_wallet_info)
abstract class WalletInfoListModel : DataBindingEpoxyModel() {
    @EpoxyAttribute
    var walletInfo: WalletInfo? = null

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.walletInfo, walletInfo)
        }
    }
}