package wacode.yamada.yuki.nempaymentapp.room.address_book

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity
data class FriendWallet constructor(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val friendId: Long,
        val walletName: String = "",
        val walletAddress: String = "",
        val isMaster: Boolean = false
) : Serializable