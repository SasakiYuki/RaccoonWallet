package wacode.yamada.yuki.nempaymentapp.store.creator

import io.reactivex.Observable.empty
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.store.type.MyProfileInfoActionType
import wacode.yamada.yuki.nempaymentapp.usecase.MyProfileInfoUseCase

class MyProfileInfoActionCreator(private val useCase: MyProfileInfoUseCase,
                                 private val dispatch: (MyProfileInfoActionType) -> Unit) : DisposableMapper() {
    private val myAddressCountSubject: PublishSubject<Unit> = PublishSubject.create()

    init {
        myAddressCountSubject
                .flatMap {
                    useCase.countAllMyAddress()
                            .toObservable()
                            .doOnNext {
                                dispatch(MyProfileInfoActionType.WalletInfoCount(it))
                            }
                            .doOnNext {
                                // do nothing
                            }
                            .onErrorResumeNext(empty())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe()
                .let { disposables.add(it) }
    }

    fun countUpMyAddress() {
        myAddressCountSubject.onNext(Unit)
    }
}