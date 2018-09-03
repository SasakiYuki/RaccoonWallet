package wacode.yamada.yuki.nempaymentapp.view.controller

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.epoxy.AddressBookListModel_
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem


class AddressBookListController : TypedEpoxyController<ArrayList<FriendInfoItem>>() {
    @AutoModel
    lateinit var addressBookRowModel: AddressBookListModel_

    override fun buildModels(data: ArrayList<FriendInfoItem>?) {
        data?.let {
            addList(data)
        }
    }

    private fun addList(data: ArrayList<FriendInfoItem>) {
        for (item in data) {
            AddressBookListModel_()
                    .id(modelCountBuiltSoFar)
                    .friendInfoItem(item)
                    .addTo(this)
        }
    }
}
