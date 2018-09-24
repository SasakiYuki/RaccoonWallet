package wacode.yamada.yuki.nempaymentapp.rest.item

import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo


data class FriendInfoItem(val friendInfo: FriendInfo, val isChecked: Boolean = false)