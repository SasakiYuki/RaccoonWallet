package wacode.yamada.yuki.nempaymentapp.`object`

import wacode.yamada.yuki.nempaymentapp.model.PaymentQREntity

data class PaymentQR(
        val addr: String,
        val amount: Long,
        val msg: String?,
        val name: String? = "",
        val intentType: IntentType = IntentType.ADDRESS
) {
    companion object {

        fun convert(qrEntity: PaymentQREntity): PaymentQR {
            val data = qrEntity.data
            return PaymentQR(data.addr,
                    data.amount,
                    data.msg,
                    data.name)
        }
    }

    enum class IntentType {
        ADDRESS,
        AMOUNT,
        MESSAGE,
    }
}