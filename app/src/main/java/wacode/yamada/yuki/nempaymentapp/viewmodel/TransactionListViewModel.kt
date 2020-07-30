package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.ryuta46.nemkotlin.model.MosaicId
import com.ryuta46.nemkotlin.model.TransactionMetaDataPair
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicFullItem
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicItem
import wacode.yamada.yuki.nempaymentapp.rest.model.MosaicAppEntity
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

        getAllTransaction(myAddress = address, id = transactionId)
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

    private fun getAllTransaction(myAddress: String, id: Int = -1): Observable<TransactionAppEntity> {
        return useCase.getAllTransaction(address = myAddress, id = id)
                .flatMap { transactionPair ->
                    getSenderAddress(myAddress, transactionPair)
                            .zipWith(getFullMosaicItems(transactionPair), BiFunction { senderAddress: String, mosaicItems: List<MosaicFullItem> ->
                                return@BiFunction if (transactionPair.transaction.recipient.isNullOrEmpty()) {
                                    if (transactionPair.transaction.otherTrans?.recipient != myAddress) {
                                        TransactionAppConverter.convert(TransactionType.OUTGOING, transactionPair, senderAddress, mosaicItems)
                                    } else {
                                        TransactionAppConverter.convert(TransactionType.INCOMING, transactionPair, senderAddress, mosaicItems)
                                    }
                                } else {
                                    if (transactionPair.transaction.recipient != myAddress) {
                                        TransactionAppConverter.convert(TransactionType.OUTGOING, transactionPair, senderAddress, mosaicItems)
                                    } else {
                                        TransactionAppConverter.convert(TransactionType.INCOMING, transactionPair, senderAddress, mosaicItems)
                                    }
                                }
                            })
                }
    }

    private fun getSenderAddress(address: String, transactionPair: TransactionMetaDataPair): Observable<String> {
        return if (transactionPair.transaction.recipient.isNullOrEmpty()) {
            if (transactionPair.transaction.otherTrans?.recipient != address) {
                Observable.just(address)
            } else {
                useCase.getAccountAddressFromPublicKey(transactionPair.transaction.signer)
            }
        } else {
            if (transactionPair.transaction.recipient != address) {
                Observable.just(address)
            } else {
                useCase.getAccountAddressFromPublicKey(transactionPair.transaction.signer)
            }
        }
    }

    private fun getFullMosaicItems(transactionPair: TransactionMetaDataPair): Observable<List<MosaicFullItem>> {
        return if (transactionPair.transaction.mosaics == null) {
            Observable.just(ArrayList())
        } else {
            Observable.fromIterable(transactionPair.transaction.mosaics)
                    .flatMap { transactionMosaic ->
                        useCase.getNamespaceMosaics(transactionMosaic.mosaicId.namespaceId)
                                .flatMap { listPair ->
                                    when {
                                        listPair.isNotEmpty() -> Observable.fromIterable(listPair)
                                                .map { Pair(it.mosaic.id.fullName, it.mosaic.divisibility) }
                                        transactionMosaic.mosaicId.fullName == MosaicId("nem", "xem").fullName -> Observable.just(Pair(MosaicId("nem", "xem").fullName, 2))
                                        else -> Observable.empty<Pair<String, Int>>()
                                    }
                                }
                                .filter { pair ->
                                    pair.first == transactionMosaic.mosaicId.fullName
                                }
                                .map {
                                    MosaicFullItem(it.second!!, MosaicItem(MosaicAppEntity.convert(transactionMosaic)))
                                }
                    }
                    .toList()
                    .toObservable()
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