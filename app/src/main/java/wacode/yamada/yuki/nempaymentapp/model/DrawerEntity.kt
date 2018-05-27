package wacode.yamada.yuki.nempaymentapp.model

import android.graphics.drawable.Drawable
import wacode.yamada.yuki.nempaymentapp.R

data class DrawerEntity(val image: Drawable,
                        val title: String,
                        val drawerType: DrawerType)

enum class DrawerType {
    MAIN,
    SUB
}

enum class DrawerItemType(val imageResource: Int,
                          val titleResource: Int,
                          val drawerType: DrawerType) {
    HOME(R.mipmap.icon_menu_home, R.string.main_navigation_home, DrawerType.MAIN),
    ADDRESS_BOOK(R.mipmap.icon_menu_addresbook, R.string.main_navigation_address_book, DrawerType.MAIN),
    MOSAIC_GALLERY(R.mipmap.icon_menu_mosaic, R.string.main_navigation_mosaic_gallery, DrawerType.MAIN),
    DONATE(R.mipmap.icon_menu_faucet, R.string.main_navigation_donate, DrawerType.MAIN),
    ABOUT(R.mipmap.icon_menu_about, R.string.main_navigation_about, DrawerType.SUB),
    HELP(R.mipmap.icon_menu_help, R.string.main_navigation_help, DrawerType.SUB),
    SETTING(R.mipmap.icon_menu_setting, R.string.main_navigation_setting, DrawerType.SUB)
}