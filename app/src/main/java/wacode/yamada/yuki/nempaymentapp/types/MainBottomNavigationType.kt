package wacode.yamada.yuki.nempaymentapp.types

import wacode.yamada.yuki.nempaymentapp.R

enum class MainBottomNavigationType(val textResource: Int, val drawableResource: Int) {
    QRLABO(R.string.main_bottom_navigation_qr_labo, R.mipmap.icon_home_qr_labo_gray),
    RECEIVE(R.string.main_bottom_navigation_receive, R.mipmap.icon_home_receive_gray),
    HOME(R.string.main_bottom_navigation_home, R.mipmap.icon_home_home_gray),
    SEND(R.string.main_bottom_navigation_send, R.mipmap.icon_home_send_gray),
    SCAN(R.string.main_bottom_navigation_scan, R.mipmap.icon_home_scan_gray)
}