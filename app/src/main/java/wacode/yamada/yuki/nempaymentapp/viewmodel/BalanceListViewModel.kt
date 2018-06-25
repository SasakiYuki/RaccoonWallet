package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ryuta46.nemkotlin.model.Mosaic
import com.ryuta46.nemkotlin.model.MosaicDefinitionMetaDataPair
import io.reactivex.disposables.CompositeDisposable
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicFullItem
import wacode.yamada.yuki.nempaymentapp.usecase.BalanceListUsecase
import java.util.HashMap
import javax.inject.Inject
import kotlin.collections.ArrayList

class BalanceListViewModel @Inject constructor(
        private val usecase: BalanceListUsecase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val fullItemMosaic: MutableLiveData<MosaicFullItem> = MutableLiveData()
    val errorTextResource: MutableLiveData<Int> = MutableLiveData()

    private fun getOwnedMosaicFullData(address: String) {
        usecase.getOwnedMosaics(address)
                .subscribe({
                    getMosaicList(it)
                }, { it.printStackTrace() })
                .let { compositeDisposable.add(it) }
    }

    private fun getMosaicList(list: List<Mosaic>) {
        val nameSpaceHashMap = HashMap<String, List<Mosaic>>()
        for (namespaceMosaic in list) {
            val namespaceList = ArrayList<Mosaic>()
            list.filterTo(namespaceList) { namespaceMosaic.mosaicId.namespaceId == it.mosaicId.namespaceId }
            nameSpaceHashMap.put(namespaceMosaic.mosaicId.namespaceId, namespaceList)
        }
        for (key in nameSpaceHashMap.keys) {
            usecase.getNamespaceMosaics(key)
                    .subscribe({ getFullMosaicItems(nameSpaceHashMap, key, it) },
                            { it.printStackTrace() })
                    .let { compositeDisposable.add(it) }
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