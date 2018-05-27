package wacode.yamada.yuki.nempaymentapp.types

import wacode.yamada.yuki.nempaymentapp.R

enum class MainNavigationType(val textResource: Int, val drawableResource: Int) {
    MENU(R.string.menu_search,R.mipmap.icon_menu_home),
    HOME(R.string.main_navigation_home, R.mipmap.icon_menu_home),
    ADDRESSBOOK(R.string.main_navigation_address_book,R.mipmap.icon_menu_addresbook),
    MOSAICGALLERY(R.string.main_navigation_mosaic_gallery, R.mipmap.icon_menu_mosaic),
    FAUCET(R.string.main_navigation_faucet, R.mipmap.icon_menu_faucet),
    ABOUT(R.string.main_navigation_about, R.mipmap.icon_menu_about),
    HELP(R.string.main_navigation_help, R.mipmap.icon_menu_help),
    SETTING(R.string.main_navigation_setting,R.mipmap.icon_menu_setting)
}