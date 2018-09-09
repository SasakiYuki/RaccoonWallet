package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookListUseCase
import wacode.yamada.yuki.nempaymentapp.view.custom_view.BackLayerSearchView
import javax.inject.Inject


class AddressBookListViewModel @Inject constructor(private val useCase: AddressBookListUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {
    val friendInfoLiveData: MutableLiveData<FriendInfoItem> = MutableLiveData()

    fun getAllFriendInfo() {
        findPatterMatchFriendInfoByNameAndType("", BackLayerSearchView.SearchType.ALL)
    }

    fun findPatterMatchFriendInfoByNameAndType(word: String, type: BackLayerSearchView.SearchType) {
        useCase.findFriendInfoByNameAndSearchType(word, type)
                .flatMapObservable {
                    Observable.fromIterable(it)
                }
                .flatMapSingle { friendInfo ->
                    useCase.getFriendIconPath(friendInfo.id)
                            .map { FriendInfoItem(friendInfo = friendInfo, iconPath = it) }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({
                    friendInfoLiveData.value = it
                }, {
                    it.printStackTrace()
                })
                .let { addDisposable(it) }
    }
}