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
    private val insertWalletInfo: PublishSubject<WalletInfo> = PublishSubject.create()
    private val updateWalletInfo: PublishSubject<WalletInfo> = PublishSubject.create()

    init {
        insertWalletInfo
                .flatMap {
                    repository.insertWalletInfo(it)
                            .toObservable()
                            .doOnNext {
                                dispatch(ProfileAddressAddActionType.Insert(it))
                            }
                            .doOnError {
                                dispatch(ProfileAddressAddActionType.Error(it))
                            }
                            .onErrorResumeNext(Observable.empty())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe()
                .let { disposables.add(it) }
        updateWalletInfo
                .flatMap {
                    repository.updateWalletInfo(it)
                            .toObservable()
                            .doOnNext {
                                dispatch(ProfileAddressAddActionType.Update(it))
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

    fun insert(walletInfo: WalletInfo) {
        insertWalletInfo.onNext(walletInfo)
    }

    fun update(walletInfo: WalletInfo) {
        updateWalletInfo.onNext(walletInfo)
    }
}