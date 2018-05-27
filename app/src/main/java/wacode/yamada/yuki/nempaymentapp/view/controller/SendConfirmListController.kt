package wacode.yamada.yuki.nempaymentapp.view.controller

import android.content.Context
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.toDisplayAddress
import wacode.yamada.yuki.nempaymentapp.model.epoxy.SendConfirmHeaderModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.SendConfirmMainRowModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.SendConfirmSubRowModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.SimpleSendHeaderModel_
import wacode.yamada.yuki.nempaymentapp.rest.item.SendMosaicItem
import java.util.*

class SendConfirmListController(val context: Context, private val fee: Double, private val address: String, private val message: String = "") : TypedEpoxyController<ArrayList<SendMosaicItem>>() {

    override fun buildModels(data: ArrayList<SendMosaicItem>?) {
        data?.let {
            addList(data)
        }
    }

    private fun addList(data: ArrayList<SendMosaicItem>) {
        SimpleSendHeaderModel_()
                .id(modelCountBuiltSoFar)
                .addTo(this)

        SendConfirmHeaderModel_()
                .id(modelCountBuiltSoFar)
                .title(context.getString(R.string.send_confirm_fragment_amount))
                .addTo(this)

        for (item in data) {
            SendConfirmMainRowModel_()
                    .id(modelCountBuiltSoFar)
                    .sendMosaicItem(item)
                    .addTo(this)
        }

        SendConfirmHeaderModel_()
                .id(modelCountBuiltSoFar)
                .title(context.getString(R.string.send_confirm_fragment_fee))
                .addTo(this)

        SendConfirmSubRowModel_()
                .id(modelCountBuiltSoFar)
                .title(fee.toString())
                .addTo(this)

        SendConfirmHeaderModel_()
                .id(modelCountBuiltSoFar)
                .title(context.getString(R.string.send_confirm_fragment_send_address))
                .addTo(this)

        SendConfirmSubRowModel_()
                .id(modelCountBuiltSoFar)
                .title(address.toDisplayAddress())
                .addTo(this)

        SendConfirmHeaderModel_()
                .id(modelCountBuiltSoFar)
                .title(context.getString(R.string.send_confirm_fragment_send_title))
                .addIf(message != "", this)

        SendConfirmSubRowModel_()
                .id(modelCountBuiltSoFar)
                .title(message)
                .addIf(message != "", this)
    }
}