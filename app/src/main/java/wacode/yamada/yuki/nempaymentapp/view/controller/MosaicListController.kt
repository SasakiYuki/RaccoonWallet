package wacode.yamada.yuki.nempaymentapp.view.controller

import android.view.View
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.epoxy.MosaicListHeaderModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.MosaicListRowModel_
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicFullItem
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicItem

class MosaicListController(private val listener: OnMosaicListClickListener, var showHeader: Boolean, var switchState: Boolean) : TypedEpoxyController<List<MosaicFullItem>>() {
    @AutoModel
    lateinit var mosaicListHeaderModel: MosaicListHeaderModel_

    override fun buildModels(data: List<MosaicFullItem>?) {
        data?.let {
            addList(data)
        }
    }

    private fun addList(data: List<MosaicFullItem>) {
        mosaicListHeaderModel.switchChangeListener { p0, p1 -> listener.onClickHeader(p1) }
                .checked(switchState)
                .addIf(showHeader, this)

        val formatterList = ArrayList<MosaicFullItem>()
        data.filterNotTo(formatterList) { it.mosaicItem.isNEMXEMItem() }

        for (item in formatterList) {
            MosaicListRowModel_()
                    .id(modelCountBuiltSoFar)
                    .mosaicFullItem(item)
                    .itemClickListener(View.OnClickListener {
                        listener.onClickRow(item.mosaicItem)
                    })
                    .addTo(this)
        }
    }

    interface OnMosaicListClickListener {
        fun onClickHeader(switchState: Boolean)
        fun onClickRow(item: MosaicItem)
    }
}