package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.ryuta46.nemkotlin.model.AccountMetaDataPair
import com.ryuta46.nemkotlin.model.HarvestInfo
import com.ryuta46.nemkotlin.model.TransactionMetaDataPair
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.usecase.HomeUseCase
import javax.inject.Inject


class HomeViewModel @Inject constructor(private val useCase: HomeUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {
    val accountLiveData: MutableLiveData<AccountMetaDataPair> = MutableLiveData()
    val transactionLiveData: MutableLiveData<List<TransactionMetaDataPair>> = MutableLiveData()
    val harvestLiveData: MutableLiveData<List<HarvestInfo>> = MutableLiveData()

    fun getAllData(address: String) {
        useCase.getAccountInfo(address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({
                    accountLiveData.value = it
                }, {
                    it.printStackTrace()
                }).let { addDisposable(it) }

        useCase.getAllTransaction(address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({
                    transactionLiveData.value = it
                }, {
                    it.printStackTrace()
                }).let { addDisposable(it) }

        useCase.getHarvestInfo(address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({
                    harvestLiveData.value = it
                }, {
                    it.printStackTrace()
                }).let { addDisposable(it) }
    }
}