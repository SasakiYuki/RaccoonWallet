package wacode.yamada.yuki.nempaymentapp.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_balance.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicFullItem
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.controller.BalanceListController
import wacode.yamada.yuki.nempaymentapp.viewmodel.BalanceListViewModel
import javax.inject.Inject

class BalanceActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: BalanceListViewModel

    private lateinit var controller: BalanceListController
    private val mosaics = ArrayList<MosaicFullItem>()

    override fun setLayout() = R.layout.activity_balance

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BalanceListViewModel::class.java)

        setupViewModelObserve()
        async(UI) {
            val wallet = bg { WalletManager.getSelectedWallet(this@BalanceActivity) }.await()
            wallet?.let {
                viewModel.getOwnedMosaicFullData(it.address)
            }
        }
        setupViews()
    }

    private fun setupViewModelObserve() {
        viewModel.run {
            loadingStatus.observe(this@BalanceActivity, Observer<Boolean> {
                it ?: return@Observer
                progress.visibility = if (it) View.VISIBLE else View.INVISIBLE
            })
            fullItemMosaic.observe(this@BalanceActivity, Observer {
                it ?: return@Observer
                mosaics.add(it)
                recycler.visibility = View.VISIBLE
                balanceEmptyRootView.visibility = View.GONE
                controller.setData(mosaics)
            })
        }
    }

    private fun setupViews() {
        setToolbarTitle(R.string.balance_activity_title)
        setToolBarBackButton()

        recycler.layoutManager = LinearLayoutManager(this)
        controller = BalanceListController()
        recycler.adapter = controller.adapter
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, BalanceActivity::class.java)
            return intent
        }
    }
}