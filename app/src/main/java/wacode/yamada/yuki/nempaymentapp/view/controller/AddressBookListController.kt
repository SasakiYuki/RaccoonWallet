package wacode.yamada.yuki.nempaymentapp.view.controller

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.epoxy.AddressBookListModel_
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem


class AddressBookListController(private val listener: OnAddressBookClickListener) : TypedEpoxyController<ArrayList<FriendInfoItem>>() {

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
                    .itemClickListener(View.OnClickListener {
                        listener.onClickItem(item.friendInfo.id)
                    })
                    .checkBoxChangeListener({ _, isChecked ->
                        listener.onItemChecked(item.friendInfo.id, isChecked)
                    })
                    .addTo(this)
        }
    }

    interface OnAddressBookClickListener {
        fun onClickItem(friendId: Long)

        fun onItemChecked(friendId: Long, isChecked: Boolean)
    }
}
