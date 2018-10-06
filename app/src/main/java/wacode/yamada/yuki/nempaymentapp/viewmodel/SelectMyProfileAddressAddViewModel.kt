package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.remove
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.store.SelectMyProfileAddressAddStore
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletAddEntity
import javax.inject.Inject

class SelectMyProfileAddressAddViewModel @Inject constructor(private val store: SelectMyProfileAddressAddStore) : BaseViewModel() {
    val walletListLiveData: MutableLiveData<List<Wallet>>
            = MutableLiveData()
    val myAddressLiveData: MutableLiveData<MyAddress>
            = MutableLiveData()
    val walletInfoLiveData: MutableLiveData<WalletInfo>
            = MutableLiveData()
    val onChangedAddWalletLiveData : MutableLiveData<ArrayList<WalletAddEntity>>
            = MutableLiveData()
    private val wallets: ArrayList<Wallet> = ArrayList()
    val walletAddList: ArrayList<WalletAddEntity> = ArrayList()

    init {
        store.getter.allWalletObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    walletListLiveData.value = it
                    wallets.addAll(it)
                    walletAddList.addAll(convertWalletToWalletAddEntity(it))
                })
                .let {
                    addDisposable(it)
                }
        store.getter.myAddressObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    myAddressLiveData.value = it
                })
                .let {
                    addDisposable(it)
                }
        store.getter.walleInfoObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    walletInfoLiveData.value = it
                })
                .let {
                    addDisposable(it)
                }
    }

    fun filterWallet(walletInfo: WalletInfo) {
        val list = ArrayList<WalletAddEntity>()
        walletAddList.filterTo(list) { walletInfo.walletAddress.remove("-") != it.walletAddress }
        walletAddList.clear()
        walletAddList.addAll(list)
    }

    fun findAllWallet() {
        store.actionCreator.findAllWallet()
    }

    fun findAllMyAddress() {
        store.actionCreator.findAllMyAddress()
    }

    fun select(id: Long) {
        store.actionCreator.selectMyWalletInfo(id)
    }

    fun onChangedAddWalletEntity(walletAddEntity: WalletAddEntity) {
        val list = ArrayList<WalletAddEntity>()
        for(item in walletAddList) {
            if (item.walletAddress == walletAddEntity.walletAddress) {
                list.add(walletAddEntity)
            } else {
                list.add(item)
            }
        }
        walletAddList.clear()
        walletAddList.addAll(list)
        onChangedAddWalletLiveData.value = walletAddList
    }

    private fun convertWalletToWalletAddEntity(list: List<Wallet>): ArrayList<WalletAddEntity> {
        return Observable.fromIterable(list)
                .map {
                    WalletAddEntity(it.name, it.address, true)
                }
                .toList()
                .blockingGet() as ArrayList<WalletAddEntity>
    }
}