package wacode.yamada.yuki.nempaymentapp.room.address_book

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity
data class FriendInfo constructor(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val lastName: String = "",
        val firstName: String = "",
        val lastNameKana: String = "",
        val firstNameKana: String = "",
        val phoneNumber: String = "",
        val mailAddress: String = "",
        val isTwitterAuth: Boolean = false
) : Serializable