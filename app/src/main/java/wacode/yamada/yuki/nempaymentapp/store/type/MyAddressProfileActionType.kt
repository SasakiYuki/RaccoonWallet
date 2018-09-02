package wacode.yamada.yuki.nempaymentapp.store.type

import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress

sealed class MyAddressProfileActionType {
    class Create(val myAddress: MyAddress) : MyAddressProfileActionType()
}