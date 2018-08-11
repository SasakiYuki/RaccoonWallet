package wacode.yamada.yuki.nempaymentapp.model

import android.net.Uri
import java.io.Serializable

data class PaymentQREntity(
        val v: Int = 2,
        val type: Int = 2,
        val data: PaymentQRInnerEntity
) : Serializable {
        companion object {
                fun convert(url: Uri): PaymentQREntity? {
                        url.apply {
                                val address = getQueryParameter("addr") ?: ""
                                val amount = (getQueryParameter("amount") ?: "").toLong()
                                val msg = getQueryParameter("msg") ?: ""
                                val name = getQueryParameter("name") ?: ""
                                return PaymentQREntity(data = PaymentQRInnerEntity(
                                        addr = address,
                                        amount = amount,
                                        msg = msg,
                                        name = name
                                ))
                        }
                        return null
                }
        }
}
data class PaymentQRInnerEntity(
        val addr: String,
        val amount: Long,
        val msg: String,
        val name: String? = ""
) : Serializable

