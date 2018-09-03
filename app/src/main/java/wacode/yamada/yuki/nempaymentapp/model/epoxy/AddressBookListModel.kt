package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
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

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let { binding ->
            friendInfoItem?.let { item ->
                binding.setVariable(BR.friendInfoItem, item)

                if (binding is RowAddressBookBinding) {
                    if (item.iconPath.isNotEmpty()) {
                        Picasso.with(binding.root.context)
                                .load(item.iconPath)
                                .placeholder(R.mipmap.icon_default_profile)
                                .error(R.mipmap.icon_default_profile)
                                .into(binding.circleImageView)
                    }
                }
            }
        }
    }
}