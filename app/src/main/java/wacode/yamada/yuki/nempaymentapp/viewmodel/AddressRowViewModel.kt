package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.view.View
import wacode.yamada.yuki.nempaymentapp.room.address.Address

interface AddressRowEventHandler {
    fun onAddressClick(view: View, viewModel: AddressRowViewModel)
}

interface AddressRowLongEventHandlers {
    fun onAddressLongClick(view:View,viewModel: AddressRowViewModel):Boolean
}
data class AddressRowViewModel(
        val address: Address
) {
    fun name() = address.name
    fun address() = address.address
}
