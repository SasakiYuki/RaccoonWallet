package wacode.yamada.yuki.nempaymentapp.rest.item

/**
 * モザイクデータの完全版
 * /account/mosaic/owned
 * と
 * /namespace/mosaic/definition/page
 * を叩きマッチョ合成することにより生成される激重モデル
 * ダイソー店員「（モザイク関連データは）そこに無ければ無いですね」
 */
data class MosaicFullItem(val divisibility:Int) {

}