package wacode.yamada.yuki.nempaymentapp.model

data class WalletQrEntity(
        val v:Int = 2,
        val type:Int = 2,
        val data:WalletQrInnerEntity
)

data class WalletQrInnerEntity(
        val name:String,
        val priv_key:String,
        val salt:String
)