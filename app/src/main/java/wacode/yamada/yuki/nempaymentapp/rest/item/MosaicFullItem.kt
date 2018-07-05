package wacode.yamada.yuki.nempaymentapp.rest.item

import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble

/**
 * モザイクデータの完全版
 * /account/mosaic/owned
 * と
 * /namespace/mosaic/definition/page
 * を叩きマッチョ合成することにより生成される激重モデル
 * ダイソー店員「（モザイク関連データは）そこに無ければ無いですね」
 */
data class MosaicFullItem(val divisibility: Int, val mosaicItem: MosaicItem) {
    fun getFullName() = mosaicItem.mosaic.mosaicId.fullName
    fun getMosaicBalance() = if (mosaicItem.isNEMXEMItem()) mosaicItem.mosaic.quantity.convertNEMFromMicroToDouble().toString() else (mosaicItem.mosaic.quantity / Math.pow(10.0, divisibility.toDouble())).toString()
}