package wacode.yamada.yuki.nempaymentapp.model

import wacode.yamada.yuki.nempaymentapp.types.NodeType

data class NodeEntity(
        val nodeType: NodeType,
        val nodeName: String,
        val isSelected: Boolean = false
)