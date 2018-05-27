package wacode.yamada.yuki.nempaymentapp.rest.item

import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble
import wacode.yamada.yuki.nempaymentapp.rest.model.MosaicAppEntity
import wacode.yamada.yuki.nempaymentapp.rest.model.MosaicIdAppEntity
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import java.io.Serializable

data class MosaicItem(val mosaic: MosaicAppEntity, val checked: Boolean = false) : Serializable {
    fun getFullName() = mosaic.mosaicId.fullName
    fun getQuantity() = mosaic.quantity.toString()
    fun getMosaicBalance() = if (isNEMXEMItem()) mosaic.quantity.convertNEMFromMicroToDouble().toString() else mosaic.quantity.toString()
    fun isNEMXEMItem() = mosaic.mosaicId.namespaceId == NemCommons.DEFAULT_NEM_NAMESPACE && mosaic.mosaicId.name == NemCommons.DEFAULT_NEM_NAME

    companion object {
        fun createNEMXEMItem() = MosaicItem(MosaicAppEntity(MosaicIdAppEntity(NemCommons.DEFAULT_NEM_NAMESPACE, NemCommons.DEFAULT_NEM_NAME), 0))
    }
}