package wacode.yamada.yuki.nempaymentapp.view.controller

import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletInfoListModel_
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

class WalletInfoListController() : TypedEpoxyController<List<WalletInfo>?>() {
    override fun buildModels(data: List<WalletInfo>?) {
        data?.let {
            for (item in it) {
                WalletInfoListModel_()
                        .walletInfo(item)
                        .addTo(this)
            }
        }
    }
}