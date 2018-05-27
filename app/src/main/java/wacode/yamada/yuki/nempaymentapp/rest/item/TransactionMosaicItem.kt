package wacode.yamada.yuki.nempaymentapp.rest.item

data class TransactionMosaicItem(val nameSpace: String, val mosaicName: String, val supply: Long, val divisibility: Int,val quantity:Long)