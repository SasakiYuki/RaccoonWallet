package wacode.yamada.yuki.nempaymentapp.utils

import android.content.Context
import com.ryuta46.nemkotlin.account.Account
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.extentions.getId
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet


object WalletManager {
    private const val SP_SELECTED_WALLET_ID = "sp_selected_wallet_id"

    fun save(context: Context, account: Account, walletName: String) {
        val salt = KeyProvider.generateSalt()
        val pinCode = PinCodeProvider.getPinCode(context)!!
        val secretKey = KeyProvider.deriveKey(pinCode.toString(Charsets.UTF_8), salt)
        val encryptData = AesCryptographer.encrypt(account.walletCompatiblePrivateKey.toByteArray(Charsets.UTF_8), secretKey)
        val id = getId()
        val wallet = Wallet(id = id,
                salt = salt,
                name = walletName,
                publicKey = account.publicKeyString,
                encryptedSecretKey = encryptData,
                address = account.address)
        async(UI) {
            bg {
                NemPaymentApplication.database.walletDao().insert(wallet)
                val wallets = NemPaymentApplication.database.walletDao().findAll()
                for (item in wallets) {
                    if (item.address == account.address) {
                        saveSelectWallet(context, item.id)
                        RxBus.send(RxBusEvent.SELECT)
                    }
                }
            }.await()
            WalletProvider.wallet = wallet
        }
    }

    fun updatePinCode(oldWallet: Wallet, newPinCode: ByteArray, oldPinCode: ByteArray) {
        val rawKey = AesCryptographer.decrypt(oldWallet.encryptedSecretKey, oldWallet.salt, oldPinCode.toString(Charsets.UTF_8))

        val salt = KeyProvider.generateSalt()
        val key = KeyProvider.deriveKey(newPinCode.toString(Charsets.UTF_8), salt)
        val newKey = AesCryptographer.encrypt(rawKey, key)
        val newWallet = Wallet(
                id = oldWallet.id,
                salt = salt,
                name = oldWallet.name,
                publicKey = oldWallet.publicKey,
                encryptedSecretKey = newKey,
                address = oldWallet.address)
        NemPaymentApplication.database.walletDao().update(newWallet)
    }

    fun getPrivateKey(context: Context, wallet: Wallet): String {
        PinCodeProvider.getPinCode(context)?.let {
            return AesCryptographer.decrypt(wallet.encryptedSecretKey, wallet.salt, it.toString(Charsets.UTF_8))
                    .toString(Charsets.UTF_8)
        }

        return ""
    }

    fun getPrivateKey(wallet: Wallet, pin: ByteArray): String {
        return AesCryptographer.decrypt(wallet.encryptedSecretKey, wallet.salt, pin.toString(Charsets.UTF_8))
                .toString(Charsets.UTF_8)
    }

    fun saveSelectWallet(context: Context, id: Long) {
        SharedPreferenceUtils.put(context, SP_SELECTED_WALLET_ID, id)
    }

    fun getSelectedWallet(context: Context): Wallet? {
        val id = SharedPreferenceUtils[context, SP_SELECTED_WALLET_ID, 0L]
        return NemPaymentApplication.database.walletDao().getById(id)
    }

    fun getSelectedWalletId(context: Context): Long {
        return SharedPreferenceUtils[context, SP_SELECTED_WALLET_ID, 0L]
    }

    fun getWalletbyId(walletId: Long): Wallet {
        return NemPaymentApplication.database.walletDao().getById(walletId)
    }
}