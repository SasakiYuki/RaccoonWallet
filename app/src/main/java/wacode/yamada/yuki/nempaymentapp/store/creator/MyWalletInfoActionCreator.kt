package wacode.yamada.yuki.nempaymentapp.store.creator

import io.reactivex.Observable.empty
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.store.type.MyWalletInfoActionType
import wacode.yamada.yuki.nempaymentapp.usecase.MyWalletInfoUseCase

class MyWalletInfoActionCreator(private val usecase: MyWalletInfoUseCase,
                                private val dispatch: (MyWalletInfoActionType) -> Unit) : DisposableMapper() {

    private val myAddressSubject: PublishSubject<Unit> = PublishSubject.create()
    private val selectWalletInfoSubject: PublishSubject<Long> = PublishSubject.create()

    init {
        myAddressSubject
                .flatMap {
                    usecase.findAllMyAddress()
                            .doOnNext {
                                dispatch(MyWalletInfoActionType.ReceiveMyAddress(it))
                            }
                            .doOnError {
                                // do nothing
                            }
                            .onErrorResumeNext(empty())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe()
                .let { disposables.add(it) }

        selectWalletInfoSubject
                .flatMap {
                    usecase.select(it)
                            .toObservable()
                            .doOnNext {
                                dispatch(MyWalletInfoActionType.SelectWalletInfo(it))
                            }
                            .doOnError {
                                // do nothing
                            }
                            .onErrorResumeNext(empty())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe()
                .let { disposables.add(it) }
    }

    fun findAllMyAddress() {
        myAddressSubject.onNext(Unit)
    }

    fun selectWalletInfo(id: Long) {
        selectWalletInfoSubject.onNext(id)
    }

}