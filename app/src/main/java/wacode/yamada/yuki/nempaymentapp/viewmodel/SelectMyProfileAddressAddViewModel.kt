package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import javax.inject.Inject

class SelectMyProfileAddressAddViewModel @Inject constructor() : BaseViewModel(){
    val walletListLiveData:MutableLiveData<List<Wallet>>
            = MutableLiveData()
    val myAddressLiveData:MutableLiveData<MyAddress>
            = MutableLiveData()
    val walletInfoLiveData:MutableLiveData<WalletInfo>
            = MutableLiveData()

    init {

    }

    fun findAllWallet(){

    }

    fun findAllMyAddress() {

    }

    fun select(id:Long) {

    }
}