package wacode.yamada.yuki.nempaymentapp.room.address_book

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity
data class FriendInfo constructor(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,
        var name: String = "",
        var nameRuby: String = "",
        var phoneNumber: String = "",
        var mailAddress: String = "",
        var isTwitterAuth: Boolean = false
) : Serializable