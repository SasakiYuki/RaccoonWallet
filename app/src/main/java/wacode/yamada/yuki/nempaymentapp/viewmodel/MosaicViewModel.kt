package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.ryuta46.nemkotlin.model.Mosaic
import io.reactivex.disposables.CompositeDisposable
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import java.util.HashMap
import kotlin.collections.ArrayList

class MosaicViewModel {
    private val compositeDisposable = CompositeDisposable()
    val fullItemMosaic = MutableLiveData()

    private fun getOwnedMosaicFullData(address: String) {
        compositeDisposable.add(NemCommons.getAccountMosaicOwned(address)
                .subscribe({ response ->

                }, { e -> e.printStackTrace() }))
    }

    private fun a(list: List<Mosaic>) {
        val nameSpaceHashMap = HashMap<String, List<Mosaic>>()
        for (namespaceMosaic in list) {
            val namespaceList = ArrayList<Mosaic>()
            list.filterTo(namespaceList) { namespaceMosaic.mosaicId.namespaceId == it.mosaicId.namespaceId }
            nameSpaceHashMap.put(namespaceMosaic.mosaicId.namespaceId, namespaceList)
        }
        for (key in nameSpaceHashMap.keys) {
            compositeDisposable.add(
                NemCommons.getNamespaceMosaics(key)
                        .subscribe(

                        )
            )
        }
    }

    private fun

            private

    fun onPause() {
        compositeDisposable.clear()
    }
}