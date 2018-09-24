package wacode.yamada.yuki.nempaymentapp.room.address_book

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class FriendAddress constructor(
        @PrimaryKey(autoGenerate = false)
        val walletInfoId: Long,
        val friendId: Long
)