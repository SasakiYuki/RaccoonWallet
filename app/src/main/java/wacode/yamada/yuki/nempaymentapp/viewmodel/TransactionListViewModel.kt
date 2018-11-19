package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import wacode.yamada.yuki.nempaymentapp.types.TransactionType
import wacode.yamada.yuki.nempaymentapp.usecase.TransactionListUseCase
import wacode.yamada.yuki.nempaymentapp.utils.TransactionAppConverter
import javax.inject.Inject


class TransactionListViewModel @Inject constructor(private val useCase: TransactionListUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {
    val transactionLiveData: MutableLiveData<TransactionAppEntity> = MutableLiveData()
    val loadingStatusLiveData: MutableLiveData<LoadingStatus> = MutableLiveData()

    fun getInitialLoading(address: String) {
        loadingStatusLiveData.value = LoadingStatus.INITIAL_LOADING

        getUnconfirmedTransactions(address)
                .mergeWith(getAllTransaction(address))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadingStatusLiveData.value = LoadingStatus.SUCCESS
                    transactionLiveData.value = it
                }, {
                    if (it is IllegalArgumentException && it.message == ERROR_LIST_EMPTY) {
                        loadingStatusLiveData.value = LoadingStatus.EMPTY
                    }
                    it.printStackTrace()
                }).let { addDisposable(it) }
    }

    fun getLoadMore(address: String, transactionId: Int) {
        loadingStatusLiveData.value = LoadingStatus.MORE_LOADING

        getAllTransaction(address = address, id = transactionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadingStatusLiveData.value = LoadingStatus.SUCCESS
                    transactionLiveData.value = it
                }, {
                    if (it is IllegalArgumentException && it.message == ERROR_LIST_EMPTY) {
                        loadingStatusLiveData.value = LoadingStatus.COMPLETE
                    }
                    it.printStackTrace()
                }).let { addDisposable(it) }
    }

    private fun getUnconfirmedTransactions(address: String) = useCase.getUnconfirmedTransactions(address)
            .map { TransactionAppConverter.convert(TransactionType.UNCONFIRMED, it) }

    private fun getAllTransaction(address: String, id: Int = -1): Observable<TransactionAppEntity> {
        return useCase.getAllTransaction(address = address, id = id)
                .flatMap { transition ->
                    return@flatMap if (transition.transaction.recipient.isNullOrEmpty()) {
                        if (transition.transaction.otherTrans?.recipient != address) {
                            Observable.just(transition)
                                    .map {
                                        val appEntity = TransactionAppConverter.convert(TransactionType.OUTGOING, transition)
                                        appEntity.senderAddress = address
                                        return@map appEntity
                                    }
                        } else {
                            useCase.getAccountAddressFromPublicKey(transition.transaction.signer)
                                    .map {
                                        val appEntity = TransactionAppConverter.convert(TransactionType.INCOMING, transition)
                                        appEntity.senderAddress = it
                                        return@map appEntity
                                    }
                        }
                    } else {
                        return@flatMap if (transition.transaction.recipient != address) {
                            Observable.just(transition)
                                    .map {
                                        val appEntity = TransactionAppConverter.convert(TransactionType.OUTGOING, transition)
                                        appEntity.senderAddress = address
                                        return@map appEntity
                                    }
                        } else {
                            useCase.getAccountAddressFromPublicKey(transition.transaction.signer)
                                    .map {
                                        val appEntity = TransactionAppConverter.convert(TransactionType.INCOMING, transition)
                                        appEntity.senderAddress = it
                                        return@map appEntity
                                    }
                        }
                    }
                }
    }

    companion object {
        const val ERROR_LIST_EMPTY = "list is empty"
    }

    enum class LoadingStatus {
        INITIAL_LOADING,
        MORE_LOADING,
        SUCCESS,
        COMPLETE,
        EMPTY
    }
}