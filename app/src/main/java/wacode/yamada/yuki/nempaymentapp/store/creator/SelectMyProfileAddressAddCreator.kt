package wacode.yamada.yuki.nempaymentapp.store.creator

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.store.type.SelectMyProfileAddressAddActionType
import wacode.yamada.yuki.nempaymentapp.usecase.SelectMyProfileAddressAddUseCase

class SelectMyProfileAddressAddCreator(private val useCase: SelectMyProfileAddressAddUseCase,
                                       private val dispatch: (SelectMyProfileAddressAddActionType) -> Unit) : DisposableMapper() {
    private val findAllWallet: PublishSubject<Unit> = PublishSubject.create()
    private val findAllMyAddress: PublishSubject<Unit> = PublishSubject.create()
    private val selectWalletInfo: PublishSubject<Long> = PublishSubject.create()

    init {
        findAllWallet
                .flatMap {
                    useCase.findAllWallet()
                            .toObservable()
                            .doOnNext {
                                dispatch(SelectMyProfileAddressAddActionType.ReceiveAllWallet(it))
                            }
                            .onErrorResumeNext(Observable.empty())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe()
                .let { disposables.add(it) }
        findAllMyAddress
                .flatMap {
                    useCase.findAllMyAddress()
                            .doOnNext {
                                dispatch(SelectMyProfileAddressAddActionType.ReceiveMyAddress(it))
                            }
                            .onErrorResumeNext(Observable.empty())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe()
                .let { disposables.add(it) }
        selectWalletInfo
                .flatMap {
                    useCase.select(it)
                            .toObservable()
                            .doOnNext {
                                dispatch(SelectMyProfileAddressAddActionType.ReceiveWalletInfo(it))
                            }
                            .onErrorResumeNext(Observable.empty())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe()
                .let { disposables.add(it) }
    }

    fun findAllWallet() {
        findAllWallet.onNext(Unit)
    }

    fun findAllMyAddress() {
        findAllMyAddress.onNext(Unit)
    }

    fun selectMyWalletInfo(id: Long) {
        selectWalletInfo.onNext(id)
    }

}