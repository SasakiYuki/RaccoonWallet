package wacode.yamada.yuki.nempaymentapp.helper

import android.content.Context
import org.mindrot.jbcrypt.BCrypt
import wacode.yamada.yuki.nempaymentapp.preference.PinCodePreference

object PinCodeHelper {
    fun isAvailable(context: Context) = PinCodePreference.getHash(context).isNotEmpty()

    fun createHash(pinCode: String) = BCrypt.hashpw(pinCode, BCrypt.gensalt(10))

    fun checkPinCode(context: Context, rawPinCode: String): Boolean {
        val hash = PinCodePreference.getHash(context)

        if (hash.isEmpty()) return false

        return BCrypt.checkpw(rawPinCode, hash)
    }
}