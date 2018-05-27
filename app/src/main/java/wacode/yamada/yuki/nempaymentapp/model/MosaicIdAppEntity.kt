package wacode.yamada.yuki.nempaymentapp.model

import java.io.Serializable

data class MosaicIdAppEntity(
        val namespaceId: String,
        val name: String) : Serializable {
    val fullName: String get() = "$namespaceId:$name"
}