package wacode.yamada.yuki.nempaymentapp.view.controller

import android.view.View
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import wacode.yamada.yuki.nempaymentapp.types.NodeType
import wacode.yamada.yuki.nempaymentapp.model.NodeEntity
import wacode.yamada.yuki.nempaymentapp.model.epoxy.SelectNodeHeaderModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.SelectNodeModel_
import wacode.yamada.yuki.nempaymentapp.model.epoxy.SelectNodeSectionModel_
import java.util.*


class SelectNodeListController(private val listener: SelectNodeListController.OnSelectNodeClickListener) : TypedEpoxyController<ArrayList<NodeEntity>>() {
    @AutoModel
    lateinit var selectNodeHeaderModel: SelectNodeHeaderModel_

    @AutoModel
    lateinit var selectNodeSectionModel: SelectNodeSectionModel_

    override fun buildModels(data: ArrayList<NodeEntity>?) {
        data?.let {
            addList(data)
        }
    }

    private fun addList(data: ArrayList<NodeEntity>) {
        selectNodeHeaderModel?.let {
            it.addIf(data.isNotEmpty(), this)
        }

        selectNodeSectionModel?.let {
            it.addIf(data.isNotEmpty(), this)
        }

        for (item in data) {
            SelectNodeModel_()
                    .id(modelCountBuiltSoFar)
                    .nodeEntity(item)
                    .itemClickListener(View.OnClickListener {
                        listener.onClickItem(item.nodeType)
                    })
                    .radioButtonClickListener(View.OnClickListener {
                        listener.onClickRadioButton(item.nodeType)
                    })
                    .addTo(this)
        }
    }

    interface OnSelectNodeClickListener {
        fun onClickItem(nodeType: NodeType)
        fun onClickRadioButton(nodeType: NodeType)
    }
}