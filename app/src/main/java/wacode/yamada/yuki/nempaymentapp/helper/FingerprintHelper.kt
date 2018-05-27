package wacode.yamada.yuki.nempaymentapp.helper

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import wacode.yamada.yuki.nempaymentapp.preference.FingerprintPreference
import wacode.yamada.yuki.nempaymentapp.preference.PinCodePreference
import java.io.ByteArrayOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


object FingerprintHelper {
    private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
    private const val KEY_PROVIDER = "AndroidKeyStore"
    private const val KEY_ALIAS = "NemPaymentAppAlias"
    private const val CHARACTER_SET_UTF_8 = "UTF-8"
    private const val AUTHENTICATION_VALIDITY_DURATION = 3

    fun checkForSave(context: Context): Boolean {
        val fingerprintManager = FingerprintManagerCompat.from(context)
        val isValid = FingerprintPreference.getDialogState(context)
        return fingerprintManager.hasEnrolledFingerprints() && isValid
    }

    fun checkForLaunch(context: Context): Boolean {
        val fingerprintManager = FingerprintManagerCompat.from(context)
        val isValid = FingerprintPreference.getDialogState(context)
        val existData = PinCodePreference.getEncryptData(context).isNotEmpty() && PinCodePreference.getIv(context).isNotEmpty()
        val isFingerprintSetting = FingerprintPreference.getFingerprintSetting(context)
        return fingerprintManager.hasEnrolledFingerprints() && isValid && existData && isFingerprintSetting
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun createKeyPair(keyStore: KeyStore) {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_PROVIDER)

            keyGenerator.init(KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationValidityDurationSeconds(AUTHENTICATION_VALIDITY_DURATION)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build())
            keyGenerator.generateKey()
        }
    }

    fun initAndGetKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(KEY_PROVIDER)
        keyStore.load(null)
        return keyStore
    }

    fun initAndGetCipherObject(keyStore: KeyStore, iv: ByteArray): Cipher {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val key = keyStore.getKey(KEY_ALIAS, null) as SecretKey

        val ips = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, key, ips)
        return cipher
    }

    fun encrypt(keyStore: KeyStore, plainText: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv

        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, cipher)
        cipherOutputStream.write(plainText.toByteArray(charset(CHARACTER_SET_UTF_8)))
        cipherOutputStream.close()

        return Pair(outputStream.toByteArray(), iv)
    }

    fun decrypt(cipher: Cipher, encryptedData: ByteArray) = String(cipher.doFinal(encryptedData), charset(CHARACTER_SET_UTF_8))
}