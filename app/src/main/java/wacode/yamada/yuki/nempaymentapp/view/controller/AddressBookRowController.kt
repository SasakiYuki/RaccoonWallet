package wacode.yamada.yuki.nempaymentapp.view.controller

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.epoxy.AddressBookRowModel_
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem


class AddressBookRowController : TypedEpoxyController<ArrayList<FriendInfoItem>>() {
    @AutoModel
    lateinit var addressBookRowModel: AddressBookRowModel_

    override fun buildModels(data: ArrayList<FriendInfoItem>?) {
        data?.let {
            addList(data)
        }
    }

    private fun addList(data: ArrayList<FriendInfoItem>) {
        for (item in data) {
            AddressBookRowModel_()
                    .id(modelCountBuiltSoFar)
                    .friendInfoItem(item)
                    .addTo(this)
        }
    }
}
