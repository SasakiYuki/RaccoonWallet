package wacode.yamada.yuki.nempaymentapp.room.wallet

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import wacode.yamada.yuki.nempaymentapp.extentions.toDisplayAddress
import java.io.Serializable


@Entity
data class Wallet constructor(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val salt: ByteArray,
        val name: String,
        val publicKey: String,
        val encryptedSecretKey: ByteArray,
        val address: String,
        val isMultisig: Boolean = false
) : Serializable {
    fun displayAddress() = address.toDisplayAddress()
}