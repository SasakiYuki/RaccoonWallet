package wacode.yamada.yuki.nempaymentapp.view.controller

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.epoxy.BalanceListHeaderModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.BalanceListModel_
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicFullItem

class BalanceListController : TypedEpoxyController<ArrayList<MosaicFullItem>>() {
    @AutoModel
    lateinit var balanceListHeaderModel: BalanceListHeaderModel_

    override fun buildModels(data: ArrayList<MosaicFullItem>?) {
        data?.let {
            addList(data)
        }
    }

    private fun addList(data: ArrayList<MosaicFullItem>) {
        balanceListHeaderModel?.let {
            it.mosaicItem(data[0].mosaicItem)
        }

        for (item in data) {
            BalanceListModel_()
                    .id(modelCountBuiltSoFar)
                    .mosaicFullItem(item)
                    .addTo(this)
        }
    }
}