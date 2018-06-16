package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ryuta46.nemkotlin.model.Mosaic
import com.ryuta46.nemkotlin.model.MosaicDefinitionMetaDataPair
import io.reactivex.disposables.CompositeDisposable
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicFullItem
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import java.util.HashMap
import kotlin.collections.ArrayList

class MosaicViewModel :ViewModel(){
    private val compositeDisposable = CompositeDisposable()
    val fullItemMosaic: MutableLiveData<MosaicFullItem> = MutableLiveData()

    private fun getOwnedMosaicFullData(address: String) {
        compositeDisposable.add(NemCommons.getAccountMosaicOwned(address)
                .subscribe({ response ->
                    getMosaicList(response)
                }, { e -> e.printStackTrace() }))
    }

    private fun getMosaicList(list: List<Mosaic>) {
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
                                getFullMosaicItems(nameSpaceHashMap, key, item)
                            }, {})
            )
        }
    }

    private fun getFullMosaicItems(nameSpaceHashMap: HashMap<String, List<Mosaic>>, key: String, responseList: List<MosaicDefinitionMetaDataPair>) {
        val mosaicList = nameSpaceHashMap[key]
        mosaicList?.let {
            for (responseItem in responseList) {
                it
                        .filter { responseItem.mosaic.id.fullName == it.mosaicId.fullName }
                        .forEach {
                            responseItem.mosaic.divisibility?.let {
                                fullItemMosaic.value = MosaicFullItem(it)
                            }
                        }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}