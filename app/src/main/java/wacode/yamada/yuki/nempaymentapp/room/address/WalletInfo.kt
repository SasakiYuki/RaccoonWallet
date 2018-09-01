package wacode.yamada.yuki.nempaymentapp.room.address

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity data class WalletInfo
constructor(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val walletName: String = "",
        val walletAddress: String = "",
        val isMaster: Boolean = false
) : Serializable