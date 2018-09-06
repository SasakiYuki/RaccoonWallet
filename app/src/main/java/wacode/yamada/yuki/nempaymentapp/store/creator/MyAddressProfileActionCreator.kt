package wacode.yamada.yuki.nempaymentapp.store.creator

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.repository.MyAddressProfileRepository
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.store.type.MyAddressProfileActionType

class MyAddressProfileActionCreator(private val repository: MyAddressProfileRepository,
                                    private val dispatch: (MyAddressProfileActionType) -> Unit) : DisposableMapper() {
    private val createSubject: PublishSubject<MyAddress> = PublishSubject.create()

    init {
        createSubject
                .flatMap {
                    repository.create(it)
                            .toObservable<Unit>()
                            .doOnComplete {
                                dispatch(MyAddressProfileActionType.Create())
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

    fun create(myAddress: MyAddress) {
        createSubject.onNext(myAddress)
    }
}