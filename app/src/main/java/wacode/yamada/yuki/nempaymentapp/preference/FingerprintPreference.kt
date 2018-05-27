package wacode.yamada.yuki.nempaymentapp.preference

import android.content.Context
import wacode.yamada.yuki.nempaymentapp.utils.SharedPreferenceUtils

object FingerprintPreference {
    private const val KEY_IS_VALID_FINGERPRINT = "key_is_valid_fingerprint"
    private const val KEY_IS_FINGERPRINT_SETTING = "key_is_fingerprint_setting"

    fun saveDialogState(context: Context, isValid: Boolean) {
        SharedPreferenceUtils.put(context, KEY_IS_VALID_FINGERPRINT, isValid)
    }

    fun getDialogState(context: Context) = SharedPreferenceUtils.get(context, KEY_IS_VALID_FINGERPRINT, true)

    fun saveFingerprintSetting(context: Context, isValid: Boolean) {
        SharedPreferenceUtils.put(context, KEY_IS_FINGERPRINT_SETTING, isValid)
    }

    fun getFingerprintSetting(context: Context) = SharedPreferenceUtils[context, KEY_IS_FINGERPRINT_SETTING, false]
}