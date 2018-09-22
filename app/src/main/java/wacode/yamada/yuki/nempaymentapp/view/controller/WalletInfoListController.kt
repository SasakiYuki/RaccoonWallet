package wacode.yamada.yuki.nempaymentapp.view.controller

import android.view.View
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletInfoEmptyModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletInfoListModel_
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

class WalletInfoListController(val walletInfoClickListener: WalletInfoClickListener) : TypedEpoxyController<List<WalletInfo>?>() {
    @AutoModel
    lateinit var walletInfoEmptyModel: WalletInfoEmptyModel_

    override fun buildModels(data: List<WalletInfo>?) {
        data?.let {
            walletInfoEmptyModel.addIf(it.isEmpty(), this)
            for (item in it) {
                WalletInfoListModel_()
                        .id(modelCountBuiltSoFar)
                        .walletInfo(item)
                        .onClickRowListener(View.OnClickListener {
                            walletInfoClickListener.onRowClick(item)
                        })
                        .addTo(this)
            }
        } ?: run {
            walletInfoEmptyModel.addTo(this)
        }
    }
}

interface WalletInfoClickListener {
    fun onRowClick(walletInfo: WalletInfo)
}