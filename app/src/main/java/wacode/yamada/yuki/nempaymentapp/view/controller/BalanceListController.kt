package wacode.yamada.yuki.nempaymentapp.view.controller

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.epoxy.BalanceListHeaderModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.BalanceListModel_
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicItem

class BalanceListController : TypedEpoxyController<ArrayList<MosaicItem>>() {
    @AutoModel
    lateinit var balanceListHeaderModel: BalanceListHeaderModel_

    override fun buildModels(data: ArrayList<MosaicItem>?) {
        data?.let {
            addList(data)
        }
    }

    private fun addList(data: ArrayList<MosaicItem>) {
        balanceListHeaderModel?.let {
            it.mosaicItem(data[0])
        }

        for (item in data) {
            BalanceListModel_()
                    .id(modelCountBuiltSoFar)
                    .mosaicItem(item)
                    .addTo(this)
        }
    }
}