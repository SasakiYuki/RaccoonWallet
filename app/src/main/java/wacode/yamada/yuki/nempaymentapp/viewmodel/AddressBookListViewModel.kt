package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfoSortType
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookListUseCase
import wacode.yamada.yuki.nempaymentapp.view.custom_view.BackLayerSearchView
import javax.inject.Inject


class AddressBookListViewModel @Inject constructor(private val useCase: AddressBookListUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {
    val friendInfoLiveData: MutableLiveData<FriendInfoItem> = MutableLiveData()

    fun findFriendInfo(word: String = "", searchType: BackLayerSearchView.SearchType = BackLayerSearchView.SearchType.ALL, sortType: FriendInfoSortType = FriendInfoSortType.NAME) {
        useCase.findFriendInfo(word, searchType, sortType)
                .flatMapObservable {
                    Observable.fromIterable(it)
                }
                .map { FriendInfoItem(friendInfo = it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    friendInfoLiveData.value = it
                }, {
                    it.printStackTrace()
                })
                .let { addDisposable(it) }
    }

    fun removeAndGetAllFriendInfo(deleteList: List<FriendInfoItem>) {
        Observable.fromIterable(deleteList)
                .filter {
                    it.isChecked
                }
                .observeOn(Schedulers.io())
                .flatMapCompletable {
                    useCase.removeFriendInfo(it.friendInfo.id)
                            .andThen(useCase.removeAddressBook(it.friendInfo.id))
                            .onErrorComplete()
                }
                .andThen(useCase.findFriendInfo("", BackLayerSearchView.SearchType.ALL, FriendInfoSortType.NAME))
                .flatMapObservable {
                    Observable.fromIterable(it)
                }
                .map { FriendInfoItem(friendInfo = it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    friendInfoLiveData.value = it
                }, {
                    it.printStackTrace()
                })
                .let { addDisposable(it) }
    }
}