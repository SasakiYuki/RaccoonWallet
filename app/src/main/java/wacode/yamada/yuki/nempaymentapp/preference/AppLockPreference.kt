package wacode.yamada.yuki.nempaymentapp.preference

import android.content.Context
import wacode.yamada.yuki.nempaymentapp.utils.SharedPreferenceUtils

object AppLockPreference {
    private const val KEY_IS_VALID_APP_LOCK = "key_is_valid_app_lock"

    fun save(context: Context, isAvailable: Boolean) {
        SharedPreferenceUtils.put(context, KEY_IS_VALID_APP_LOCK, isAvailable)
    }

    fun isAvailable(context: Context) = SharedPreferenceUtils[context, KEY_IS_VALID_APP_LOCK, false]
}