package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.support.v7.widget.AppCompatCheckBox
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

    @EpoxyAttribute
    var walletAddEntity: WalletAddEntity? = null

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.entity, walletAddEntity)
            val checkBox = it.root.findViewById<AppCompatCheckBox>(R.id.checkbox)
            it.root.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
                onClickRowListener.onClick(it)
            }
            checkBox.setOnClickListener {
                onClickRowListener.onClick(checkBox)
            }
        }
    }
}