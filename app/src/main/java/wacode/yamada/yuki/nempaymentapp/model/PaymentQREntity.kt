package wacode.yamada.yuki.nempaymentapp.model

import android.content.Intent
import android.net.Uri
import com.airbnb.deeplinkdispatch.DeepLink
import java.io.Serializable

data class PaymentQREntity(
        val v: Int = 2,
        val type: Int = 2,
        val data: PaymentQRInnerEntity
) : Serializable {
    companion object {
        private const val KEY_ADDRESS = "addr"
        private const val KEY_AMOUNT = "amount"
        private const val KEY_MESSAGE = "msg"
        private const val KEY_NAME = "name"
        fun convert(url: Uri): PaymentQREntity? {
            url.apply {
                val address = getQueryParameter(KEY_ADDRESS) ?: ""
                val amount = (getQueryParameter(KEY_AMOUNT) ?: "0").toLong()
                val msg = getQueryParameter(KEY_MESSAGE) ?: ""
                val name = getQueryParameter(KEY_NAME) ?: ""
                return PaymentQREntity(data = PaymentQRInnerEntity(
                        addr = address,
                        amount = amount,
                        msg = msg,
                        name = name
                ))
            }
            return null
        }

        fun convert(intent: Intent): PaymentQREntity? {
            intent.apply {
                return if (getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
                    val address = extras.getString(KEY_ADDRESS) ?: ""
                    val amount = (extras.getString(KEY_AMOUNT) ?: "0").toLong()
                    val message = extras.getString(KEY_MESSAGE) ?: ""
                    val name = extras.getString(KEY_NAME) ?: ""
                    PaymentQREntity(data = PaymentQRInnerEntity(
                            addr = address,
                            amount = amount,
                            msg = message,
                            name = name
                    ))
                } else {
                    null
                }
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

