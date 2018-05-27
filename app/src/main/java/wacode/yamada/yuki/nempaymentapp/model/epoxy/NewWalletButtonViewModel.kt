package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.view.View
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R

@EpoxyModelClass(layout = R.layout.row_new_wallet_button)
abstract class NewWalletButtonViewModel : DataBindingEpoxyModel() {
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var createButtonClickListener: View.OnClickListener = View.OnClickListener { }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding!!.setVariable(BR.createButtonClickListener, createButtonClickListener)
    }
}