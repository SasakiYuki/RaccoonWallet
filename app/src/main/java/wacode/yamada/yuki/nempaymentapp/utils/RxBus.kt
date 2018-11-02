package wacode.yamada.yuki.nempaymentapp.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


object RxBus {
    fun toObservable(): Observable<Any> {
        return bus
    }

    private val bus: PublishSubject<Any> = PublishSubject.create()

    fun send(event: Any) {
        bus.onNext(event)
    }

    fun <T> receive(eventType: Class<T>): Observable<T> = bus.ofType(eventType)
}

enum class RxBusEvent {
    SELECT,
    RENAME,
    REMOVE
}
