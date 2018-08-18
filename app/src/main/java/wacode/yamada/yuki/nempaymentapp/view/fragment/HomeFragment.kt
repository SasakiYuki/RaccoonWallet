package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ryuta46.nemkotlin.model.AccountMetaDataPair
import com.ryuta46.nemkotlin.model.HarvestInfo
import com.ryuta46.nemkotlin.model.TransactionMetaDataPair
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.utils.*
import wacode.yamada.yuki.nempaymentapp.view.activity.BalanceActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.CreateAddressBookActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.TransactionActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.`interface`.SimpleCallbackDoubleInterface
import wacode.yamada.yuki.nempaymentapp.view.dialog.`interface`.SimpleNoInterface
import java.text.NumberFormat

class HomeFragment : BaseFragment() {
    private var shouldDoRefreshView = false
    private var wallet: Wallet? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun layoutRes() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRxBus()
        setupWallet()
        setupSwipeRefresh()
    }

    override fun onResume() {
        super.onResume()
        if (shouldDoRefreshView) {
            shouldDoRefreshView = false
            setupWallet()
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
        showProgress()

        wallet?.let {
            compositeDisposable.add(
                    NemCommons.getAccountInfo(it.address)
                            .subscribe({ response ->
                                setupAccountViews(accountMetaDataPair = response)
                                hideProgress()
                                hideSwipeRefreshIcon()
                            }, { e ->
                                e.printStackTrace()
                                hideProgress()
                                hideSwipeRefreshIcon()
                                context?.showToast(R.string.home_fragment_error_account)
                            })
            )
            compositeDisposable.add(
                    NemCommons.getAccountTransfersAll(it.address)
                            .subscribe({ response ->
                                setupTransactionItems(response)
                                hideProgress()
                                hideSwipeRefreshIcon()
                            }, { e ->
                                e.printStackTrace()
                                hideSwipeRefreshIcon()
                                hideProgress()
                                context?.showToast(R.string.home_fragment_error_transaction)
                            })
            )
            compositeDisposable.add(
                    NemCommons.getHarvestInfo(it.address)
                            .subscribe({ response ->
                                setupHarvestItem(response)
                                hideSwipeRefreshIcon()
                                hideProgress()
                            }, { e ->
                                e.printStackTrace()
                                hideSwipeRefreshIcon()
                                hideProgress()
                                context?.showToast(R.string.home_fragment_error_transaction)
                            })
            )
        }

        if (wallet == null) {
            hideProgress()
            harvestEmptyView.visibility = View.VISIBLE
            transactionEmptyView.visibility = View.VISIBLE
        }
    }

    private fun hideSwipeRefreshIcon() {
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
            //            context?.showToast(R.string.com_coming_soon)
            startActivity(Intent(context, CreateAddressBookActivity::class.java))
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

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    companion object {
        public fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            return fragment
        }
    }
}