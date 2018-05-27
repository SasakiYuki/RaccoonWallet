package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.view.View
import wacode.yamada.yuki.nempaymentapp.extentions.transformMosaicImageUrl
import wacode.yamada.yuki.nempaymentapp.rest.model.MosaicIdEntity

interface MosaicRowEventHandler {
    fun onMosaicClick(view: View, viewModel: MosaicRowViewModel)
}

interface MosaicRowLongEventHandler {
    fun onMosaicLongClick(view: View, viewModel: MosaicRowViewModel): Boolean
}

data class MosaicRowViewModel(val mosaicIdEntity: MosaicIdEntity) {
    fun imageUrl() = mosaicIdEntity.imageUrl?.transformMosaicImageUrl()
}