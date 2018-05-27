package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.view.View
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.model.SimpleWalletEntity

@EpoxyModelClass(layout = R.layout.row_choose_wallet)
abstract class ChooseWalletModel : DataBindingEpoxyModel() {
    @EpoxyAttribute
    var simpleWallet: SimpleWalletEntity? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var rowClickListener: View.OnClickListener = View.OnClickListener { }

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var settingClickListener: View.OnClickListener = View.OnClickListener { }

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var radioClickListener: View.OnClickListener = View.OnClickListener { }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.simpleWallet, simpleWallet)
            it.setVariable(BR.rowClickListener, rowClickListener)
            it.setVariable(BR.settingClickListener, settingClickListener)
            it.setVariable(BR.radioClickListener, radioClickListener)
        }
    }
}