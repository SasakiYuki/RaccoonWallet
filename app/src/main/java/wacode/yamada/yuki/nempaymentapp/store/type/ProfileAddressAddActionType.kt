package wacode.yamada.yuki.nempaymentapp.store.type

sealed class ProfileAddressAddActionType {
    class Create : ProfileAddressAddActionType()
    class Error(throwable: Throwable) : ProfileAddressAddActionType()
}