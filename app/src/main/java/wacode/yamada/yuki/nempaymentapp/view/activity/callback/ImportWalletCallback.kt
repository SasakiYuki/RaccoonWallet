package wacode.yamada.yuki.nempaymentapp.view.activity.callback


interface ImportWalletCallback {
    fun onReplaceImportWalletName(key: ByteArray)

    fun navigateCompleteImportWallet()
}