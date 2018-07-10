package wacode.yamada.yuki.nempaymentapp.rest.item

import wacode.yamada.yuki.nempaymentapp.model.PaymentQREntity
import java.io.Serializable

data class PaymentQrItem(private val paymentQREntity: PaymentQREntity) : Serializable {
    fun existAmount() = paymentQREntity.data.amount > 0

    fun existMessage() = paymentQREntity.data.msg.isNullOrEmpty()

    companion object {
        fun createItem(paymentQREntity: PaymentQREntity) = PaymentQrItem(paymentQREntity)
    }
}