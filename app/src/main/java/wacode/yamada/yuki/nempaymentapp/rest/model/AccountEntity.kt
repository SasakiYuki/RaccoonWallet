package wacode.yamada.yuki.nempaymentapp.rest.model

data class AccountDataEntity(
        val account: AccountEntity,
        val meta: MetaEntity
)
data class AccountEntity(
        val address:String,
        val balance:Long,
        val vestedBalance:Long,
        val importance:Double,
        val publicKey:String
)

data class MetaEntity(
        val status:String,
        val remoteStatus:String
)