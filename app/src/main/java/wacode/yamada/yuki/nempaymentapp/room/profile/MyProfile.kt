package wacode.yamada.yuki.nempaymentapp.room.profile

import java.io.Serializable

data class MyProfile constructor(
        val name: String = "",
        val nameRuby: String = "",
        val phoneNumber: String = "",
        val mailAddress: String = "",
        val iconPath: String = "",
        val screenPath: String = "",
        val isTwitterAuth: Boolean = false
) : Serializable
