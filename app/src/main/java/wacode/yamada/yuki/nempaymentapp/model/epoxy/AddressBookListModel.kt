package wacode.yamada.yuki.nempaymentapp.model.epoxy

import android.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem


@EpoxyModelClass(layout = R.layout.row_address_book)
abstract class AddressBookListModel : DataBindingEpoxyModel() {
    @EpoxyAttribute
    var friendInfoItem: FriendInfoItem? = null

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.let {
            it.setVariable(BR.friendInfoItem, friendInfoItem)
        }
    }
}