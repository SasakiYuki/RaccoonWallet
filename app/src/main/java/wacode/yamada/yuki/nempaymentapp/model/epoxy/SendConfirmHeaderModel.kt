package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R

@EpoxyModelClass(layout = R.layout.row_header_send_confirm)
abstract class SendConfirmHeaderModel : DataBindingEpoxyModel() {
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var title:String? = null

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.title,title)
        }
    }
}
