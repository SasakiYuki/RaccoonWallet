package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R

@EpoxyModelClass(layout = R.layout.row_header_drawer_list)
abstract class DrawerHeaderModel : DataBindingEpoxyModel() {
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var name: String = ""

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var address: String = ""

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding!!.setVariable(BR.name, name)
        binding!!.setVariable(BR.address, address)
    }
}