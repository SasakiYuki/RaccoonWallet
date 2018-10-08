package wacode.yamada.yuki.nempaymentapp.store.creator

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.store.type.MyAddressProfileActionType
import wacode.yamada.yuki.nempaymentapp.usecase.MyAddressProfileUseCase

class MyAddressProfileActionCreator(private val useCase: MyAddressProfileUseCase,
                                    private val dispatch: (MyAddressProfileActionType) -> Unit) : DisposableMapper() {
    private val insertMyAddressSubject: PublishSubject<MyAddress> = PublishSubject.create()
    private val insertWalletInfoSubject: PublishSubject<WalletInfo> = PublishSubject.create()

    init {
        insertMyAddressSubject
                .flatMap {
                    useCase.insertMyAddress(it)
                            .toObservable<Unit>()
                            .doOnComplete {
                                dispatch(MyAddressProfileActionType.InsertMyAddress())
                            }
                            .doOnError {
                                // do nothing
                            }
                            .onErrorResumeNext(Observable.empty())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe()
                .let { disposables.add(it) }

        insertWalletInfoSubject
                .flatMap {
                    useCase.insertWalletInfo(it)
                            .toObservable()
                            .doOnNext {
                                dispatch(MyAddressProfileActionType.InsertWalletInfo(it))
                            }
                            .doOnError {
                                // do nothing
                            }
                            .onErrorResumeNext(Observable.empty())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe()
                .let { disposables.add(it) }
    }

    fun insertMyAddress(myAddress: MyAddress) {
        insertMyAddressSubject.onNext(myAddress)
    }

    fun insertWalletInfo(walletInfo: WalletInfo) {
        insertWalletInfoSubject.onNext(walletInfo)
    }
}