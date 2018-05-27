package wacode.yamada.yuki.nempaymentapp.rest.model

import java.io.Serializable

data class MosaicEntity(
        val creator:String,
        val description:String,
        val id:MosaicIdEntity
)

data class MosaicIdEntity(
        val namespaceId: String,
        val name: String,
        val imageUrl: String?
):Serializable

data class OwnedMosaicDefinitionEntity(
        val data: ArrayList<MosaicEntity>
)
