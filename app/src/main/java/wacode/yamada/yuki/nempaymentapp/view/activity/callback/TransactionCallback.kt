package wacode.yamada.yuki.nempaymentapp.view.activity.callback

import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity


interface TransactionCallback {
    fun onReplaceTransactionDetail(entity: TransactionAppEntity)
}