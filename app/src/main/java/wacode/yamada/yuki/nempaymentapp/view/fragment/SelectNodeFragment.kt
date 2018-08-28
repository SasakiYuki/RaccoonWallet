package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragmnet_select_node.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.helper.ActiveNodeHelper
import wacode.yamada.yuki.nempaymentapp.model.NodeEntity
import wacode.yamada.yuki.nempaymentapp.types.NodeType
import wacode.yamada.yuki.nempaymentapp.view.controller.SelectNodeListController


class SelectNodeFragment : BaseFragment(), SelectNodeListController.OnSelectNodeClickListener {
    private lateinit var controller: SelectNodeListController
    private var selectNodeType: NodeType? = null

    override fun layoutRes() = R.layout.fragmnet_select_node

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = SelectNodeListController(this)
        setupViews()
    }

    override fun onPause() {
        super.onPause()
        context?.let {
            ActiveNodeHelper.saveNodeType(it, selectNodeType)
        }
    }

    private fun setupViews() {
        recyclerView.layoutManager = LinearLayoutManager(this@SelectNodeFragment.context)
        recyclerView.adapter = controller.adapter

        controller.setData(createNodeList())
    }

    private fun createNodeList(): ArrayList<NodeEntity> {
        return Observable.fromIterable(ActiveNodeHelper.activeNodeList)
                .map { it ->
                    if (it.equals(ActiveNodeHelper.selectedNodeType)) {
                        NodeEntity(it, it.nodeName, true)
                    } else {
                        NodeEntity(it, it.nodeName, false)
                    }
                }
                .toList()
                .blockingGet() as ArrayList<NodeEntity>
    }

    private fun refreshData(nodeType: NodeType): ArrayList<NodeEntity> {
        return Observable.fromIterable(createNodeList())
                .map {
                    if (it.nodeType.equals(nodeType)) {
                        NodeEntity(nodeType, it.nodeName, true)
                    } else {
                        NodeEntity(it.nodeType, it.nodeName, false)
                    }
                }
                .toList()
                .blockingGet() as ArrayList<NodeEntity>
    }

    override fun onClickItem(nodeType: NodeType) {
        controller.setData(refreshData(nodeType))
        selectNodeType = nodeType
    }

    override fun onClickRadioButton(nodeType: NodeType) {
        controller.setData(refreshData(nodeType))
        selectNodeType = nodeType
    }

    companion object {
        fun newInstance(): SelectNodeFragment {
            val fragment = SelectNodeFragment()
            val args = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.setting_node_title)
            }
            fragment.arguments = args
            return fragment
        }
    }
}