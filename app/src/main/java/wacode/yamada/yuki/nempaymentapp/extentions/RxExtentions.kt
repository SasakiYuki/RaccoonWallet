package wacode.yamada.yuki.nempaymentapp.extentions

import android.arch.lifecycle.MutableLiveData
import io.reactivex.*

interface LoadingStatus {
    val loadingStatus: MutableLiveData<Boolean>
    fun <T> Observable<T>.attachLoading(): Observable<T>
    fun <T> Single<T>.attachLoading(): Single<T>
    fun <T> Flowable<T>.attachLoading(): Flowable<T>
    fun Completable.attachLoading(): Completable
    fun <T> Maybe<T>.attachLoading(): Maybe<T>
}

class LoadingStatusImpl(override val loadingStatus: MutableLiveData<Boolean> = MutableLiveData()) : LoadingStatus {

    override fun <T> Observable<T>.attachLoading(): Observable<T>
            = this.doOnSubscribe { loadingStatus.value = true }
            .doAfterTerminate { loadingStatus.value = false }

    override fun <T> Flowable<T>.attachLoading(): Flowable<T>
            = this.doOnSubscribe { loadingStatus.value = true }
            .doAfterTerminate { loadingStatus.value = false }

    override fun Completable.attachLoading(): Completable
            = this.doOnSubscribe { loadingStatus.value = true }
            .doAfterTerminate { loadingStatus.value = false }

    override fun <T> Maybe<T>.attachLoading(): Maybe<T>
            = this.doOnSubscribe { loadingStatus.value = true }
            .doAfterTerminate { loadingStatus.value = false }

    override fun <T> Single<T>.attachLoading(): Single<T>
            = this.doOnSubscribe { loadingStatus.value = true }
            .doAfterTerminate { loadingStatus.value = false }
}
