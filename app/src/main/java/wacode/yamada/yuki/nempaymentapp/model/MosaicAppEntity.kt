package wacode.yamada.yuki.nempaymentapp.model

import com.ryuta46.nemkotlin.model.MosaicId
import java.io.Serializable

class MosaicAppEntity(mosaicId: MosaicId, val quantity: Long) : Serializable {
    var mosaicIdAppEntity: MosaicIdAppEntity = MosaicIdAppEntity(
            namespaceId = mosaicId.namespaceId,
            name = mosaicId.name
    )
}