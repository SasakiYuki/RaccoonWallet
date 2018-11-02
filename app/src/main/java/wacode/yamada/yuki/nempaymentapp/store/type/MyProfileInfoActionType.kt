package wacode.yamada.yuki.nempaymentapp.store.type

import wacode.yamada.yuki.nempaymentapp.model.MyProfileEntity

sealed class MyProfileInfoActionType {
    class WalletInfoCount(val walletCount: Int) : MyProfileInfoActionType()
    class ReceiveMyProfile(val myProfileEntity: MyProfileEntity) : MyProfileInfoActionType()
    class UpdateMyProfile(val myProfileEntity: MyProfileEntity) : MyProfileInfoActionType()
    class Error(val throwable: Throwable) : MyProfileInfoActionType()
}