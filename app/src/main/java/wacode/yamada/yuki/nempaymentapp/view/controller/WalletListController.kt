package wacode.yamada.yuki.nempaymentapp.view.controller

import android.view.View
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.SimpleWalletEntity
import wacode.yamada.yuki.nempaymentapp.model.epoxy.ChooseWalletModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.NewWalletButtonViewModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletListEmptyModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.WalletListHeaderModel_
import java.util.*

class WalletListController(private val listener: OnWalletListClickListener) : TypedEpoxyController<ArrayList<SimpleWalletEntity>>() {
    @AutoModel
    lateinit var walletListHeaderModel: WalletListHeaderModel_

    @AutoModel
    lateinit var newWalletButtonViewModel: NewWalletButtonViewModel_

    @AutoModel
    lateinit var walletListEmptyModel: WalletListEmptyModel_

    override fun buildModels(data: ArrayList<SimpleWalletEntity>?) {
        data?.let {
            addList(it)
        }
    }

    private fun addList(data: ArrayList<SimpleWalletEntity>) {
        walletListHeaderModel?.let {
            it.addTo(this)
        }

        newWalletButtonViewModel?.let {
            it.createButtonClickListener(View.OnClickListener {
                listener.onClickCreateButton()
            }).addTo(this)
        }

        walletListEmptyModel?.let {
            it.addIf(data.isEmpty(), this)
        }

        for (item in data) {
            ChooseWalletModel_()
                    .id(modelCountBuiltSoFar)
                    .simpleWallet(item)
                    .rowClickListener(View.OnClickListener {
                        listener.onClickRow(item.id)
                    })
                    .settingClickListener(View.OnClickListener {
                        listener.onClickSettingButton(item.id, item.isMultisig)
                    })
                    .radioClickListener(View.OnClickListener {
                        listener.onClickRow(item.id)
                    })
                    .addTo(this)
        }
    }

    interface OnWalletListClickListener {
        fun onClickCreateButton()
        fun onClickSettingButton(id: Long, isMultisig: Boolean)
        fun onClickRow(id: Long)
    }
}