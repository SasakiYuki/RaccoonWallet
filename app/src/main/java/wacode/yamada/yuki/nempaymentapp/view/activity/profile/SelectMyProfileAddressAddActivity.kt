package wacode.yamada.yuki.nempaymentapp.view.activity.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.R

class SelectMyProfileAddressAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_my_profile_address_add)
    }

    fun a() {
        Wallet全件取得　ViewModelに格納 AllWalletList　すべてにチェック
        MyAddress全件取得　取得次第WalletInfoをselect
        WalletInfoが返ってきたら該当のWalletが AllWalletListにあるかチェック　存在する場合そいつはAllWalletListから外す
    }

    fun findAllWallet(){

    }

}
