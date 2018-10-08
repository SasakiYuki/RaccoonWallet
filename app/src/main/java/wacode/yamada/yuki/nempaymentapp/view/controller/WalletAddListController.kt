package wacode.yamada.yuki.nempaymentapp.view.controller

import android.support.v7.widget.AppCompatCheckBox
import android.view.View
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletAddHeaderModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletAddRowModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletInfoEmptyModel_
import java.io.Serializable

class WalletAddListController(private val listener: WalletAddListClickListener) : TypedEpoxyController<ArrayList<WalletAddEntity>>() {
    @AutoModel
    lateinit var walletAddHeaderModel: WalletAddHeaderModel_
    @AutoModel
    lateinit var walletInfoEmptyModel: WalletInfoEmptyModel_

    override fun buildModels(data: ArrayList<WalletAddEntity>?) {
        data?.let {
            if (data.isNotEmpty()) {
                addList(it)
            } else {
                walletInfoEmptyModel.addTo(this)
            }
        } ?: run {
            walletInfoEmptyModel.addTo(this)
        }
    }

    private fun addList(data: ArrayList<WalletAddEntity>) {
        walletAddHeaderModel.addTo(this)
        for (item in data) {
            WalletAddRowModel_()
                    .id(modelCountBuiltSoFar)
                    .walletAddEntity(item)
                    .onClickRowListener(View.OnClickListener {
                        val checkBox = it.findViewById<AppCompatCheckBox>(R.id.checkbox)
                        listener.onClickRow(WalletAddEntity(item.walletName, item.walletAddress, checkBox.isSelected))
                    })
                    .addTo(this)
        }
    }

    interface WalletAddListClickListener {
        fun onClickRow(walletAddEntity: WalletAddEntity)
    }
}

data class WalletAddEntity(val walletName: String,
                           val walletAddress: String,
                           val isSelected: Boolean) : Serializable