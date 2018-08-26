package wacode.yamada.yuki.nempaymentapp.room.address_book

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity
data class FriendIcon constructor(
        @PrimaryKey(autoGenerate = false)
        val friendId: Long,
        val iconPath: String
) : Serializable