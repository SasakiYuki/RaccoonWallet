package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.ryuta46.nemkotlin.model.Mosaic
import com.ryuta46.nemkotlin.model.MosaicDefinitionMetaDataPair
import io.reactivex.disposables.CompositeDisposable
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicFullItem
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import java.util.HashMap
import kotlin.collections.ArrayList

class MosaicViewModel {
    private val compositeDisposable = CompositeDisposable()
    val fullItemMosaic: MutableLiveData<MosaicFullItem> = MutableLiveData()

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
                            .subscribe({ item ->
                                ab(nameSpaceHashMap, key, item)
                            }, {})
            )
        }
    }

    private fun ab(nameSpaceHashMap: HashMap<String, List<Mosaic>>, key: String, responseList: List<MosaicDefinitionMetaDataPair>) {
        val mosaicList = nameSpaceHashMap[key]
        mosaicList?.let {
            for (responseItem in responseList) {
                for (mosaicItem in it) {
                    if (responseItem.mosaic.id.fullName == mosaicItem.mosaicId.fullName) {
                        responseItem.mosaic.divisibility?.let {
                            fullItemMosaic.value = MosaicFullItem(it)
                        }
                    }
                }
            }
        }
    }

    fun onPause() {
        compositeDisposable.clear()
    }
}