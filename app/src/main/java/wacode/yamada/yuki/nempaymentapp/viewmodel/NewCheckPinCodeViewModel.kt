package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.content.Context
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper

class NewCheckPinCodeViewModel {
    var stringBuilder = StringBuilder()

    fun addPinCode(value: Int) {
        stringBuilder.append(value)
    }

    fun isMaxLength() = stringBuilder.length == PIN_MAX_LENGTH

    fun resetStringBuilder() {
        stringBuilder = StringBuilder()
    }

    fun checkPinCode(context: Context) = PinCodeHelper.checkPinCode(context, String(stringBuilder))

    fun getPinLength() = stringBuilder.length

    companion object {
        private const val PIN_MAX_LENGTH = 6
    }
}