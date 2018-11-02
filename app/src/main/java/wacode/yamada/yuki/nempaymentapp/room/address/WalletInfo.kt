package wacode.yamada.yuki.nempaymentapp.room.address

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import wacode.yamada.yuki.nempaymentapp.extentions.toDisplayAddress
import java.io.Serializable

@Entity
data class WalletInfo
constructor(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var walletName: String = "",
        var walletAddress: String = "",
        var isMaster: Boolean = false
) : Serializable {
    fun displayWalletAddress() = walletAddress.toDisplayAddress()
}