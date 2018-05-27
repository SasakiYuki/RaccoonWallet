package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.view.View
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.model.NodeEntity

@EpoxyModelClass(layout = R.layout.row_select_node)
abstract class SelectNodeModel : DataBindingEpoxyModel() {
    @EpoxyAttribute
    var nodeEntity: NodeEntity? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var itemClickListener: View.OnClickListener = View.OnClickListener { }


    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var radioButtonClickListener: View.OnClickListener = View.OnClickListener { }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.nodeEntity, nodeEntity)
            it.setVariable(BR.itemClickListener, itemClickListener)
            it.setVariable(BR.radioButtonClickListener, radioButtonClickListener)
        }
    }
}