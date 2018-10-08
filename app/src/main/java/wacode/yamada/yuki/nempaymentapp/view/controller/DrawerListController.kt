package wacode.yamada.yuki.nempaymentapp.view.controller

import android.view.View
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.model.DrawerEntity
import wacode.yamada.yuki.nempaymentapp.model.DrawerType
import wacode.yamada.yuki.nempaymentapp.model.epoxy.DrawerHeaderModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.DrawerRowModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.DrawerSectionModel_

class DrawerListController(private val listener: OnDrawerClickListener, val name: String, val screenPath: String = "", val iconPath: String = "") : TypedEpoxyController<List<DrawerEntity>>() {
    @AutoModel
    lateinit var drawerHeaderModel: DrawerHeaderModel_

    @AutoModel
    lateinit var drawerSectionModel: DrawerSectionModel_

    override fun buildModels(data: List<DrawerEntity>?) {
        data?.let {
            addList(it)
        }
    }

    private fun addList(data: List<DrawerEntity>) {
        drawerHeaderModel
                .name(name)
                .screenPath(screenPath)
                .iconPath(iconPath)
                .onClickHeaderListener(View.OnClickListener {
                    listener.onHeaderClick()
                })
                .addTo(this)

        val mainList = ArrayList<DrawerEntity>()
        data.filterTo(mainList) { it.drawerType == DrawerType.MAIN }
        val subList = ArrayList<DrawerEntity>()
        data.filterTo(subList) { it.drawerType == DrawerType.SUB }

        for (item in mainList) {
            DrawerRowModel_()
                    .id(modelCountBuiltSoFar)
                    .drawerEntity(item)
                    .onClickRowListener(View.OnClickListener {
                        listener.onRowClick(item)
                    })
                    .addTo(this)
        }

        drawerSectionModel.addTo(this)

        for (item in subList) {
            DrawerRowModel_()
                    .id(modelCountBuiltSoFar)
                    .drawerEntity(item)
                    .onClickRowListener(View.OnClickListener {
                        listener.onRowClick(item)
                    })
                    .addTo(this)
        }

    }

    interface OnDrawerClickListener {
        fun onHeaderClick()
        fun onRowClick(drawerEntity: DrawerEntity)
    }
}