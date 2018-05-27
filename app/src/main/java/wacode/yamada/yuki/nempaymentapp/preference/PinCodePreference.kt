package wacode.yamada.yuki.nempaymentapp.preference

import android.content.Context
import android.util.Base64
import wacode.yamada.yuki.nempaymentapp.helper.FingerprintHelper
import wacode.yamada.yuki.nempaymentapp.utils.SharedPreferenceUtils

object PinCodePreference {
    private const val KEY_PIN_CODE_HASH = "key_pin_code_hash"
    private const val KEY_PIN_CODE_IV = "key_pin_code_iv"
    private const val KEY_PIN_CODE_AES = "key_pin_code_aes"

    fun saveHash(context: Context, pinCodeHash: String) {
        SharedPreferenceUtils.put(context, KEY_PIN_CODE_HASH, pinCodeHash)
    }

    fun savePinCodeForFingerprint(context: Context, pinCode: String) {
        if (FingerprintHelper.checkForSave(context)) {
            val keyStore = FingerprintHelper.initAndGetKeyStore()
            FingerprintHelper.createKeyPair(keyStore)
            val data = FingerprintHelper.encrypt(keyStore, pinCode)

            FingerprintPreference.saveFingerprintSetting(context, true)
            SharedPreferenceUtils.put(context, KEY_PIN_CODE_AES, Base64.encodeToString(data.first, Base64.DEFAULT))
            SharedPreferenceUtils.put(context, KEY_PIN_CODE_IV, Base64.encodeToString(data.second, Base64.DEFAULT))
        }
    }

    fun removePinCodeForFingerprint(context: Context) {
        FingerprintPreference.saveFingerprintSetting(context, false)
        SharedPreferenceUtils.remove(context, KEY_PIN_CODE_AES)
        SharedPreferenceUtils.remove(context, KEY_PIN_CODE_IV)
    }

    fun getHash(context: Context) = SharedPreferenceUtils[context, KEY_PIN_CODE_HASH, ""]

    fun getEncryptData(context: Context) = SharedPreferenceUtils[context, KEY_PIN_CODE_AES, ""]

    fun getIv(context: Context) = SharedPreferenceUtils[context, KEY_PIN_CODE_IV, ""]
}