package wacode.yamada.yuki.nempaymentapp.utils

import android.content.Context
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper

object PinCodeProvider {
    private val defaultValue = byteArrayOf(50, 57, 54, 56, 53, 49)
    private var pinCode: ByteArray? = null

    fun setPinCode(pinCode: String) {
        this.pinCode = pinCode.toByteArray(Charsets.UTF_8)
    }

    fun getPinCode(context: Context): ByteArray? {
        pinCode?.let {
            return it
        }

        if (!PinCodeHelper.isAvailable(context)) {
            return defaultValue
        }

        return null
    }

    fun clearCache() {
        pinCode = null
    }
}