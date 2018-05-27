package wacode.yamada.yuki.nempaymentapp.utils

import org.spongycastle.crypto.BufferedBlockCipher
import org.spongycastle.crypto.engines.AESEngine
import org.spongycastle.crypto.modes.CBCBlockCipher
import org.spongycastle.crypto.paddings.PKCS7Padding
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher
import org.spongycastle.crypto.params.KeyParameter
import org.spongycastle.crypto.params.ParametersWithIV
import java.security.SecureRandom
import java.util.*


object AesCryptographer {
    private const val MIN_SALT_SIZE = 32
    private val BLOCK_SIZE = AESEngine().blockSize

    fun decrypt(encryptedData: ByteArray, salt: ByteArray, password: String): ByteArray {
        if (encryptedData.isEmpty() || salt.isEmpty() || password.isEmpty()) {
            throw IllegalArgumentException("EncryptedData , password or salt cannot be empty")
        }
        if (salt.size < MIN_SALT_SIZE) {
            throw IllegalArgumentException("Salt has different length")
        }

        val secretKey = KeyProvider.deriveKey(password, salt)
        val ivData = Arrays.copyOfRange(encryptedData, 0, BLOCK_SIZE)
        val encData = Arrays.copyOfRange(encryptedData, BLOCK_SIZE, encryptedData.size)
        val cipher = setupBlockCipher(secretKey, ivData, false)

        return decodeToRawKey(cipher, encData)
    }

    fun encrypt(data: ByteArray, secretKey: ByteArray): ByteArray {
        val ivData = ByteArray(BLOCK_SIZE)
        SecureRandom().nextBytes(ivData)

        val cipher = setupBlockCipher(secretKey, ivData, true)

        val buf = transform(cipher, data)

        val result = ByteArray(ivData.size + buf.size)
        System.arraycopy(ivData, 0, result, 0, ivData.size)
        System.arraycopy(buf, 0, result, ivData.size, buf.size)
        return result
    }

    private fun setupBlockCipher(sharedKey: ByteArray?, ivData: ByteArray, forEncryption: Boolean): BufferedBlockCipher {
        val keyParam = KeyParameter(sharedKey)
        val params = ParametersWithIV(keyParam, ivData)
        val padding = PKCS7Padding()
        val cipher = PaddedBufferedBlockCipher(CBCBlockCipher(AESEngine()), padding)
        cipher.reset()
        cipher.init(forEncryption, params)
        return cipher
    }

    private fun decodeToRawKey(cipher: BufferedBlockCipher, data: ByteArray): ByteArray {
        val buf = ByteArray(cipher.getOutputSize(data.size))
        var length = cipher.processBytes(data, 0, data.size, buf, 0)
        length += cipher.doFinal(buf, length)
        return Arrays.copyOf(buf, length)
    }

    private fun transform(cipher: BufferedBlockCipher, data: ByteArray): ByteArray {
        val buf = ByteArray(cipher.getOutputSize(data.size))
        var length = cipher.processBytes(data, 0, data.size, buf, 0)
        length += cipher.doFinal(buf, length)

        return Arrays.copyOf(buf, length)
    }

}