package wacode.yamada.yuki.nempaymentapp.rest.model

import com.ryuta46.nemkotlin.model.Mosaic
import java.io.Serializable

data class MosaicAppEntity(
        val mosaicId: MosaicIdAppEntity,
        val quantity: Long
) : Serializable {
    companion object {
        fun convert(mosaic: Mosaic): MosaicAppEntity {
            return MosaicAppEntity(MosaicIdAppEntity(mosaic.mosaicId.namespaceId, mosaic.mosaicId.name), mosaic.quantity)
        }
    }
}
