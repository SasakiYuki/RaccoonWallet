package wacode.yamada.yuki.nempaymentapp.viewmodel

import io.reactivex.subjects.PublishSubject

class ChooseCreateOrScanWalletViewModel {
    val replaceEvent : PublishSubject<Unit> = PublishSubject.create()

    fun replaceFragment() {
        replaceEvent.onNext(Unit)
    }
}
