package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.widget.CompoundButton
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R

@EpoxyModelClass(layout = R.layout.row_header_mosaic_list)
abstract class MosaicListHeaderModel : DataBindingEpoxyModel() {
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var switchChangeListener: CompoundButton.OnCheckedChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, b -> }

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var checked: Boolean = false

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding!!.setVariable(BR.switchChangeListener, switchChangeListener)
        binding!!.setVariable(BR.checked, checked)
    }
}