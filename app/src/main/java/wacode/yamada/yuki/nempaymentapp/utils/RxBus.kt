package wacode.yamada.yuki.nempaymentapp.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class RxBus {
    private val bus = PublishSubject.create<RxBusEvent>()

    fun send(o: RxBusEvent) {
        bus.onNext(o)
    }

    fun toObservable(): Observable<RxBusEvent> {
        return bus
    }
}

enum class RxBusEvent {
    SELECT,
    RENAME,
    REMOVE
}
