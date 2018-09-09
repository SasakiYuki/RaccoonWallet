package wacode.yamada.yuki.nempaymentapp.store.type

sealed class MyProfileInfoActionType {
    class WalletInfoCount(val walletCount: Int) : MyProfileInfoActionType()
    class Error(val throwable: Throwable) : MyProfileInfoActionType()
}