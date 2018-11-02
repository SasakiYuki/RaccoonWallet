package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import android.view.View
import android.widget.CompoundButton
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.squareup.picasso.Picasso
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.databinding.RowAddressBookBinding
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem


@EpoxyModelClass(layout = R.layout.row_address_book)
abstract class AddressBookListModel : DataBindingEpoxyModel() {
    @EpoxyAttribute
    var friendInfoItem: FriendInfoItem? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var itemClickListener: View.OnClickListener = View.OnClickListener { }

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var checkBoxChangeListener: CompoundButton.OnCheckedChangeListener? = null

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let { binding ->
            friendInfoItem?.let { item ->
                binding.setVariable(BR.friendInfoItem, item)
                binding.setVariable(BR.itemClickListener, itemClickListener)

                checkBoxChangeListener?.let {
                    binding.setVariable(BR.checkBoxChangeListener, checkBoxChangeListener)
                }

                if (binding is RowAddressBookBinding) {
                    if (item.friendInfo.iconPath.isNotEmpty()) {
                        Picasso.with(binding.root.context)
                                .load(item.friendInfo.iconPath)
                                .placeholder(R.mipmap.icon_default_profile)
                                .error(R.mipmap.icon_default_profile)
                                .into(binding.circleImageView)
                    }
                }
            }
        }
    }
}