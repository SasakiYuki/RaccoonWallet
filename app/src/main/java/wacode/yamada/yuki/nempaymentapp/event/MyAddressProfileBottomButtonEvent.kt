package wacode.yamada.yuki.nempaymentapp.event

sealed class MyAddressProfileBottomButtonEvent {
    class OnClickEditBottomButton:MyAddressProfileBottomButtonEvent()
    class OnClickCompleteBottomButton:MyAddressProfileBottomButtonEvent()
    class OnChangeEditBottomButton:MyAddressProfileBottomButtonEvent()
}