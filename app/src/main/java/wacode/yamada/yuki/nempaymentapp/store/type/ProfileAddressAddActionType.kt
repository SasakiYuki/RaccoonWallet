package wacode.yamada.yuki.nempaymentapp.store.type

import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

sealed class ProfileAddressAddActionType {
    class Create(val walletInfo: WalletInfo) : ProfileAddressAddActionType()
    class Error(throwable: Throwable) : ProfileAddressAddActionType()
}