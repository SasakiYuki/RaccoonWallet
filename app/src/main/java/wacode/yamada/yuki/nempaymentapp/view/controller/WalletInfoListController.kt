package wacode.yamada.yuki.nempaymentapp.view.controller

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletInfoEmptyModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletInfoListModel_
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

class WalletInfoListController : TypedEpoxyController<List<WalletInfo>?>() {
    @AutoModel
    lateinit var walletInfoEmptyModel: WalletInfoEmptyModel_

    override fun buildModels(data: List<WalletInfo>?) {
        data?.let {
            walletInfoEmptyModel.addIf(it.isEmpty(), this)
            for (item in it) {
                WalletInfoListModel_()
                        .id(modelCountBuiltSoFar)
                        .walletInfo(item)
                        .addTo(this)
            }
        } ?: run {
            walletInfoEmptyModel.addTo(this)
        }
    }
}