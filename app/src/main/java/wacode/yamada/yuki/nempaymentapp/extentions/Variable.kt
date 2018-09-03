package wacode.yamada.yuki.nempaymentapp.extentions

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface ImmutableVariable<T> {
    val value: T
    val observable: Observable<T>
}

class Variable<T>(initialValue: T) : ImmutableVariable<T> {
    private val subject = BehaviorSubject.createDefault(initialValue)
    override var value: T
        get() = subject.value
        set(value) {
            subject.toSerialized().onNext(value)
        }

    override val observable: Observable<T>
        get() = subject.distinctUntilChanged()
}
