package wacode.yamada.yuki.nempaymentapp.room.profile

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class MyProfile constructor(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val name: String = "",
        val nameRuby: String = "",
        val phoneNumber: String = "",
        val mailAddress: String = "",
        val isTwitterAuth: Boolean = false
) : Serializable
