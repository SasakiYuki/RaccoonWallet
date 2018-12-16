package wacode.yamada.yuki.nempaymentapp.view.activity.drawer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_mosaic_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.ApiManager
import wacode.yamada.yuki.nempaymentapp.rest.model.MosaicEntity
import wacode.yamada.yuki.nempaymentapp.rest.model.MosaicIdEntity
import wacode.yamada.yuki.nempaymentapp.rest.model.NEMGalleryEntity
import wacode.yamada.yuki.nempaymentapp.rest.model.OwnedMosaicDefinitionEntity
import wacode.yamada.yuki.nempaymentapp.rest.service.AccountAPIService
import wacode.yamada.yuki.nempaymentapp.rest.service.NemGalleryMosaicAPIService
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseActivity
import wacode.yamada.yuki.nempaymentapp.view.adapter.MosaicImageAdapter
import wacode.yamada.yuki.nempaymentapp.view.dialog.MosaicImageDialog

class MosaicListActivity : BaseActivity() {
    private val nemGalleryMosaicList = ArrayList<NEMGalleryEntity>()
    private val compositeDisposable = CompositeDisposable()

    override fun setLayout() = R.layout.activity_mosaic_list

    private val adapter = MosaicImageAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.activity_mosaic_list_title)

        setupRecyclerView()
        setupOnClickNemIcon()
    }

    override fun onResume() {
        super.onResume()
        getMosaicListFromNEMBook()
    }

    private fun setupOnClickNemIcon() {
        nemIcon.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun setupRecyclerView() {
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, 3)
        adapter.onClickHandlers = {
            val dialog = MosaicImageDialog.createDialog(it)
            dialog.show(supportFragmentManager, MosaicImageDialog::class.java.toString())
        }
    }

    private fun getMosaicListFromNEMBook() {
        showProgress()
        compositeDisposable.add(
                ApiManager.builderNemBook()
                        .create(NemGalleryMosaicAPIService::class.java)
                        .getMosaicGallery()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            nemGalleryMosaicList.addAll(it)
                            CoroutineScope(Dispatchers.Main).launch {
                                val wallet = async(Dispatchers.IO) {
                                    WalletManager.getSelectedWallet(this@MosaicListActivity)
                                }.await()
                                wallet?.let {
                                    getOwnedMosaic(it.address)
                                }
                            }
                        }, {
                            hideProgress()
                            it.printStackTrace()
                        }))
    }

    private fun getOwnedMosaic(address: String) {
        compositeDisposable.add(
                ApiManager.builder()
                        .create(AccountAPIService::class.java)
                        .getOwnedMosaicDefinition(address)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            hideProgress()
                            val list = matchMosaicGalleryData(it)
                            getAllMosaicDataFromNamespace(list)
                        }, {
                            it.printStackTrace()
                            hideProgress()
                        }))
    }

    private fun matchMosaicGalleryData(ownedMosaicEntity: OwnedMosaicDefinitionEntity): ArrayList<MosaicEntity> {
        val data = ownedMosaicEntity.data
        val list = ArrayList<MosaicEntity>()
        for (item in data) {
            var corrected = false
            for (galleryItem in nemGalleryMosaicList) {
                if (galleryItem.name == item.id.name && galleryItem.namespace == item.id.namespaceId) {
                    val idItem = MosaicIdEntity(galleryItem.namespace, galleryItem.name, galleryItem.url)
                    list.add(MosaicEntity(item.creator, item.description, idItem))
                    corrected = true
                }
            }
            if (!corrected) {
                list.add(item)
            }
        }
        return list
    }

    private fun getAllMosaicDataFromNamespace(list: ArrayList<MosaicEntity>) {
        for (item in list) {
            if (item.id.imageUrl == null) {
                if (item.description.contains("oa:")) {
                    emptyView.visibility = View.GONE
                    adapter.addItem(MosaicIdEntity(item.id.namespaceId, item.id.name, item.description))
                    adapter.notifyDataSetChanged()
                }
            } else {
                emptyView.visibility = View.GONE
                adapter.addItem(item.id)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        hideProgress()
        compositeDisposable.clear()
    }

    companion object {
        const val RESULT_MOSAIC_LIST = 1129
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, MosaicListActivity::class.java)
            return intent
        }
    }
}
