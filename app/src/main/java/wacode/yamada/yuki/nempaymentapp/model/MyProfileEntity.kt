package wacode.yamada.yuki.nempaymentapp.model

import java.io.Serializable

data class MyProfileEntity constructor(
        val name: String = "",
        val nameRuby: String = "",
        val phoneNumber: String = "",
        val mailAddress: String = "",
        val iconPath: String = "",
        val screenPath: String = "",
        val isTwitterAuth: Boolean = false
) : Serializable
