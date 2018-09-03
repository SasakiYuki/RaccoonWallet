package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.View
import com.ryuta46.nemkotlin.model.AccountMetaDataPair
import com.ryuta46.nemkotlin.model.HarvestInfo
import com.ryuta46.nemkotlin.model.TransactionMetaDataPair
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.utils.NemPricePreference
import wacode.yamada.yuki.nempaymentapp.utils.RxBusEvent
import wacode.yamada.yuki.nempaymentapp.utils.RxBusProvider
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.BalanceActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.CreateAddressBookActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.TransactionActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.`interface`.SimpleCallbackDoubleInterface
import wacode.yamada.yuki.nempaymentapp.view.dialog.`interface`.SimpleNoInterface
import wacode.yamada.yuki.nempaymentapp.viewmodel.HomeViewModel
import java.text.NumberFormat
import javax.inject.Inject


class HomeFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var shouldDoRefreshView = false
    private var wallet: Wallet? = null
    private lateinit var homeViewModel: HomeViewModel

    override fun layoutRes() = R.layout.fragment_home

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRxBus()
        setupWallet()
        setupSwipeRefresh()
        setupViewModelObserve()
    }

    override fun onResume() {
        super.onResume()
        if (shouldDoRefreshView) {
            shouldDoRefreshView = false
            setupWallet()
        }
    }

    private fun setupViewModelObserve() {
        homeViewModel.run {
            accountLiveData.observe(this@HomeFragment, Observer {
                it ?: return@Observer
                setupAccountViews(it)
            })

            transactionLiveData.observe(this@HomeFragment, Observer {
                it ?: return@Observer
                setupTransactionItems(it)
            })

            harvestLiveData.observe(this@HomeFragment, Observer {
                it ?: return@Observer
                setupHarvestItem(it)
            })

            loadingStatus.observe(this@HomeFragment, Observer {
                it ?: return@Observer
                if (it) showProgressView() else hideProgressView()
            })
        }
    }

    private fun setupWallet() {
        context?.let {
            async(UI) {
                wallet = bg { WalletManager.getSelectedWallet(it) }.await()
                setupViews()
            }
        }
    }

    private fun setupRxBus() {
        RxBusProvider.rxBus
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    when (it) {
                        RxBusEvent.SELECT, RxBusEvent.REMOVE -> {
                            shouldDoRefreshView = true
                        }
                    }
                }
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.nemGreen, R.color.nemBlue, R.color.nemOrange)
        swipeRefreshLayout.setOnRefreshListener {
            setupBalanceItem()
        }
    }

    private fun setupViews() {
        showTransactionButton.setOnClickListener {
            async(UI) {
                val wallet = bg { WalletManager.getSelectedWallet(showTransactionButton.context) }.await()
                when {
                    wallet == null -> showTransactionButton.context.showToast(R.string.home_fragment_not_select_wallet)
                    else -> startActivity(TransactionActivity.getCallingIntent(showTransactionButton.context, wallet!!))
                }
            }
        }
        setupBalanceItem()

        balanceRootView.setOnClickListener {
            startActivity(BalanceActivity.createIntent(balanceRootView.context))
        }
    }

    private fun setupBalanceItem() {
        wallet?.let {
            homeViewModel.getAllData(it.address)
        }

        if (wallet == null) {
            hideProgressView()
            harvestEmptyView.visibility = View.VISIBLE
            transactionEmptyView.visibility = View.VISIBLE
        }
    }

    private fun showProgressView() {
        if (!swipeRefreshLayout.isRefreshing) {
            showProgress()
        }
    }

    private fun hideProgressView() {
        hideProgress()

        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupHarvestItem(list: List<HarvestInfo>) {
        if (list.isNotEmpty()) {
            miniHarvestItemView.setupHarvest(list[0])
        } else {
            harvestEmptyView.visibility = View.VISIBLE
        }

        showHarvestButton.setOnClickListener {
            startActivity(CreateAddressBookActivity.createIntent(context!!))
//            context?.showToast(R.string.com_coming_soon)
        }
    }

    private fun setupTransactionItems(list: List<TransactionMetaDataPair>) {
        val address = wallet!!.address
        val selectedList = ArrayList<TransactionMetaDataPair>()
        list.filterTo(selectedList) { it.transaction.mosaics == null || (it.transaction.mosaics != null && it.transaction.mosaics!!.isEmpty()) }
        if (selectedList.isNotEmpty()) {
            transactionEmptyView.visibility = View.GONE
            miniTransactionItemView1.setupTransaction(address, list[0])
            if (selectedList.size > 1) {
                miniTransactionItemView2.setupTransaction(address, list[1])
            } else {
                miniTransactionItemView2.visibility = View.GONE
            }

            if (selectedList.size > 2) {
                miniTransactionItemView3.setupTransaction(address, list[2])
            } else {
                miniTransactionItemView3.visibility = View.GONE
            }

            if (selectedList.size > 3) {
                miniTransactionItemView4.setupTransaction(address, list[3])
            } else {
                miniTransactionItemView4.visibility = View.GONE
            }
        }
    }


    private fun setupAccountViews(accountMetaDataPair: AccountMetaDataPair?) {
        accountMetaDataPair?.let {
            balanceText.text = NumberFormat.getNumberInstance().format(it.account.balance.convertNEMFromMicroToDouble())
            setupNemPriceView(it.account.balance.convertNEMFromMicroToDouble())
        }
    }

    private fun setupNemPriceView(balance: Double) {
        NemPricePreference.getNemPriceFromZaif(object : SimpleCallbackDoubleInterface {
            override fun onCall(nemPrice: Double) {
                jpyText.text = (getString(R.string.home_fragment_jpy_before) + NumberFormat.getNumberInstance().format(nemPrice * balance))
            }
        }, object : SimpleNoInterface {
            override fun onClickNo() {
                context?.showToast(R.string.nem_converter_error_price)
            }
        })
    }

    companion object {
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            return fragment
        }
    }
}