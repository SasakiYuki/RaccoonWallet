package wacode.yamada.yuki.nempaymentapp.helper

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.types.NodeType
import wacode.yamada.yuki.nempaymentapp.rest.ApiManager
import wacode.yamada.yuki.nempaymentapp.rest.service.NodeExplorerApiService
import wacode.yamada.yuki.nempaymentapp.utils.SharedPreferenceUtils


object ActiveNodeHelper {
    private const val SP_KEY_NODE_TYPE = "sp_key_node_type"
    var activeNodeList: List<NodeType>? = null
    var selectedNodeType: NodeType? = null

    fun auto(context: Context): Completable {
        return ApiManager.builderNodeExplorer()
                .create(NodeExplorerApiService::class.java)
                .getActiveNodeList()
                .doOnSuccess({ nodeList ->
                    if (nodeList.nodes.isEmpty()) {
                        throw RuntimeException("active node list is empty")
                    }

                    activeNodeList = checkActiveNode(nodeList.nodes)
                    saveNodeType(context, checkSelectedNode(context))
                })
                .subscribeOn(Schedulers.newThread())
                .toCompletable()
    }

    private fun checkActiveNode(activeNodeList: List<String>): List<NodeType> {
        return Observable.fromIterable(NodeType.values().toList())
                .filter({ it ->
                    activeNodeList.contains(it.nodeBaseUrl)
                })
                .toList()
                .blockingGet() as List<NodeType>
    }

    //選択したnodeがactiveな場合それを返し、activeでない場合orNodeをまだ選択していない場合適当なnodeを返す
    private fun checkSelectedNode(context: Context): NodeType {
        val selectedNodeName = SharedPreferenceUtils.get(context, SP_KEY_NODE_TYPE, "")

        val nodeTypes = Observable.fromIterable(activeNodeList)
                .filter({ it ->
                    it.name == selectedNodeName
                })
                .toList()
                .blockingGet() as List<NodeType>

        return if (nodeTypes.isEmpty()) {
            activeNodeList!![0]
        } else {
            nodeTypes[0]
        }
    }

    fun saveNodeType(context: Context, nodeType: NodeType?) {
        nodeType?.let {
            SharedPreferenceUtils.put(context, SP_KEY_NODE_TYPE, nodeType.name)
            this.selectedNodeType = nodeType
        }
    }
}