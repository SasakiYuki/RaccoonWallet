package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.view.View
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

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onClickRowListener: View.OnClickListener = View.OnClickListener { }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.apply {
            setVariable(BR.walletInfo, walletInfo)
            setVariable(BR.clickListener, onClickRowListener)
        }
    }
}