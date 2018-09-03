package wacode.yamada.yuki.nempaymentapp.rest.item

import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo


data class FriendInfoItem(val friendInfo: FriendInfo, val iconPath: String = "", val isChecked: Boolean = false) {
    fun getFullName() = friendInfo.lastName + " " + friendInfo.firstName
    fun isTwitterAuth() = friendInfo.isTwitterAuth
}