package wacode.yamada.yuki.nempaymentapp.model

import java.io.Serializable

data class PaymentQREntity(
        val v: Int = 2,
        val type: Int = 2,
        val data: PaymentQRInnerEntity
) : Serializable

data class PaymentQRInnerEntity(
        val addr: String,
        val amount: Long,
        val msg: String,
        val name: String? = ""
) : Serializable

