package wacode.yamada.yuki.nempaymentapp.view.controller

import android.view.View
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletAddHeaderModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletAddRowModel_

class WalletAddListController(private val listener: WalletAddListClickListener) : TypedEpoxyController<ArrayList<WalletAddEntity>>() {
    @AutoModel
    lateinit var walletAddHeaderModel: WalletAddHeaderModel_

    override fun buildModels(data: ArrayList<WalletAddEntity>?) {
        data?.let {
            addList(it)
        }
    }

    private fun addList(data: ArrayList<WalletAddEntity>) {
        walletAddHeaderModel.addTo(this)
        for (item in data) {
            WalletAddRowModel_()
                    .id(modelCountBuiltSoFar)
                    .walletAddEntity(item)
                    .onClickRowListener(View.OnClickListener {
                        listener.onClickRow()
                    })
                    .addTo(this)
        }
    }

    interface WalletAddListClickListener {
        fun onClickRow()
    }
}

data class WalletAddEntity(val walletName: String,
                           val isSelected: Boolean)