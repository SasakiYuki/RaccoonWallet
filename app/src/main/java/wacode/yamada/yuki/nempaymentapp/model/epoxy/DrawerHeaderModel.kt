package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.widget.ImageView
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R

@EpoxyModelClass(layout = R.layout.row_header_drawer_list)
abstract class DrawerHeaderModel : DataBindingEpoxyModel() {
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var name: String = ""

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var address: String = ""

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var screenPath: String = ""

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var iconPath: String = ""

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.name, name)
            it.setVariable(BR.address, address)

            it.root.apply {
                findViewById<ImageView>(R.id.userScreenImageView)?.let {
                    Picasso.with(context).load(screenPath).into(it)
                }
                findViewById<CircleImageView>(R.id.iconImageView)?.let {
                    Picasso.with(context).load(iconPath).into(it)
                }
            }
        }
    }
}