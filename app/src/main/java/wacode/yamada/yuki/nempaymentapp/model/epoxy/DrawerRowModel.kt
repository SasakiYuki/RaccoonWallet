package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.view.View
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.model.DrawerEntity

@EpoxyModelClass(layout = R.layout.row_drawer)
abstract class DrawerRowModel : DataBindingEpoxyModel() {
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onClickRowListener: View.OnClickListener = View.OnClickListener { }

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var drawerEntity: DrawerEntity? = null

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.setVariable(BR.clickListener, onClickRowListener)
        binding?.setVariable(BR.drawer, drawerEntity)
    }
}
