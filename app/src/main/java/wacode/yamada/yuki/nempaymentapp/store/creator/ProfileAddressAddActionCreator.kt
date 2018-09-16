package wacode.yamada.yuki.nempaymentapp.store.creator

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.repository.MyProfileRepository
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.store.reducer.ProfileAddressAddReducer
import wacode.yamada.yuki.nempaymentapp.store.type.ProfileAddressAddActionType

class ProfileAddressAddActionCreator(private val repository: MyProfileRepository,
                                     private val dispatch: (ProfileAddressAddActionType) -> Unit,
                                     val reducer: ProfileAddressAddReducer) : DisposableMapper() {
    private val createWalletInfo: PublishSubject<WalletInfo> = PublishSubject.create()

    init {
        createWalletInfo
                .flatMap {
                    repository.createWalletInfo(it)
                            .toObservable()
                            .doOnNext {
                                dispatch(ProfileAddressAddActionType.Create(it))
                            }
                            .doOnError {
                                dispatch(ProfileAddressAddActionType.Error(it))
                            }
                            .onErrorResumeNext(Observable.empty())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe()
                .let { disposables.add(it) }
    }

    fun create(walletInfo: WalletInfo) {
        createWalletInfo.onNext(walletInfo)
    }
}