package wacode.yamada.yuki.nempaymentapp.store.type

import wacode.yamada.yuki.nempaymentapp.room.profile.MyProfile

sealed class MyProfileInfoActionType {
    class WalletInfoCount(val walletCount: Int) : MyProfileInfoActionType()
    class ReceiveMyProfile(val myProfile: MyProfile) : MyProfileInfoActionType()
    class UpdateMyProfile(val myProfile: MyProfile) : MyProfileInfoActionType()
    class Error(val throwable: Throwable) : MyProfileInfoActionType()
}