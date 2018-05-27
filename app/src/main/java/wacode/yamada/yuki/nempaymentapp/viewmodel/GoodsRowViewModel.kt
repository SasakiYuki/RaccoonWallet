package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.view.View
import wacode.yamada.yuki.nempaymentapp.room.goods.Goods

interface GoodsRowEventHandlers {
    fun onGoodsClick(view: View, viewModel: GoodsRowViewModel)
}

interface GoodsRowLongEventHandlers {
    fun onGoodsLongClick(view:View,viewModel: GoodsRowViewModel):Boolean
}

data class GoodsRowViewModel(
        val goods: Goods
) {
    fun title() = goods.title
    fun price() = goods.price.toString() + "XEM"
    fun id() = goods.id
}