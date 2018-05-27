package wacode.yamada.yuki.nempaymentapp.model

data class SimpleWalletEntity(
        val id: Long,
        val walletName: String,
        val isMultisig: Boolean = false,
        val isSelected: Boolean = false
)