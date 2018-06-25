package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_balance.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicItem
import wacode.yamada.yuki.nempaymentapp.rest.model.MosaicAppEntity
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.controller.BalanceListController
import wacode.yamada.yuki.nempaymentapp.viewmodel.BalanceListViewModel

class BalanceActivity : BaseActivity() {
    private lateinit var controller: BalanceListController
    private val mosaics = ArrayList<MosaicItem>()
    private val balanceListViewModel: BalanceListViewModel = BalanceListViewModel()

    override fun setLayout() = R.layout.activity_balance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
        fetchOwnedMosaics()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupViews() {
        setToolbarTitle(R.string.balance_activity_title)
        setToolBarBackButton()

        recycler.layoutManager = LinearLayoutManager(this)
        controller = BalanceListController()
        recycler.adapter = controller.adapter
    }

    private fun fetchOwnedMosaics() {
        showProgress()
        async(UI) {
            val wallet = bg { WalletManager.getSelectedWallet(this@BalanceActivity.applicationContext) }.await()
            wallet?.let {
                NemCommons.getAccountMosaicOwned(it.address)
                        .subscribe({ response ->
                            hideProgress()
                            response.mapTo(mosaics) { MosaicItem(MosaicAppEntity.convert(it)) }

                            recycler.visibility = View.VISIBLE
                            balanceEmptyRootView.visibility = View.GONE
                            controller.setData(mosaics)
                        }, { e ->
                            e.printStackTrace()
                            hideProgress()
                            commonErrorHandle()
                        })
            } ?: run {
                hideProgress()
                commonErrorHandle()
            }
        }
    }

    private fun commonErrorHandle() {
        recycler.visibility = View.GONE
        balanceEmptyRootView.visibility = View.VISIBLE
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, BalanceActivity::class.java)
            return intent
        }
    }
}