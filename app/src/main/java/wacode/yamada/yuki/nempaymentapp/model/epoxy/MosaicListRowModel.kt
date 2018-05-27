package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.view.View
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicItem

@EpoxyModelClass(layout = R.layout.row_mosaic_list)
abstract class MosaicListRowModel : DataBindingEpoxyModel() {
    @EpoxyAttribute
    var mosaic: MosaicItem? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var itemClickListener: View.OnClickListener = View.OnClickListener { }

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.mosaic, mosaic)
            it.setVariable(BR.itemClickListener, itemClickListener)
        }
    }
}