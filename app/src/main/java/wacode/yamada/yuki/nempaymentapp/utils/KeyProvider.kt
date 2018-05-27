package wacode.yamada.yuki.nempaymentapp.utils

import android.security.keystore.KeyProperties
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object KeyProvider {
    private const val SALT_SIZE_BYTES = 32
    private const val DERIVED_KEY_SIZE_BITS = 256
    private const val DERIVE_KEY_ITERATIONS = 2000

    fun generateSalt(): ByteArray {
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        val salt = ByteArray(SALT_SIZE_BYTES)
        secureRandom.nextBytes(salt)
        return salt
    }

    fun deriveKey(password: String, salt: ByteArray): ByteArray {
        if (password.isEmpty() || salt.isEmpty()) {
            throw IllegalArgumentException("Password or salt cannot be empty")
        }

        val keySpec = PBEKeySpec(password.toCharArray(), salt, DERIVE_KEY_ITERATIONS, DERIVED_KEY_SIZE_BITS)

        try {
            val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes = keyFactory.generateSecret(keySpec).encoded
            val key = SecretKeySpec(keyBytes, KeyProperties.KEY_ALGORITHM_AES)
            return key.encoded
        } catch (e: InvalidKeySpecException) {
            throw InvalidKeySpecException("Failed to derive key")
        } catch (e: NoSuchAlgorithmException) {
            throw NoSuchAlgorithmException("Failed to derive key")
        }

    }
}