package wacode.yamada.yuki.nempaymentapp.flux

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class Store<AT, out AC, R, out G> where AC : DisposableMapper, R : DisposableMapper, G : DisposableMapper {
    private val dispatcher: PublishSubject<AT> = PublishSubject.create()
    private val reducer: R by lazy {
        createReducer(dispatcher)
    }
    val actionCreator: AC by lazy {
        createActionCreator({
            dispatcher.onNext(it)
        }, reducer)
    }
    val getter: G by lazy {
        createGetter(reducer)
    }

    protected abstract fun createActionCreator(dispatch: (AT) -> Unit, reducer: R): AC
    protected abstract fun createReducer(action: Observable<AT>): R
    protected abstract fun createGetter(reducer: R): G

    fun clearDisposables() {
        actionCreator.disposables.clear()
        reducer.disposables.clear()
        getter.disposables.clear()
    }
}

open class DisposableMapper {
    val disposables = CompositeDisposable()
}
