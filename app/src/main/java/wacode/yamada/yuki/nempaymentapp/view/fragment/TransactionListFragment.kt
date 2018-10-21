package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.ryuta46.nemkotlin.model.AccountMetaDataPair
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.toDisplayAddress
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.types.TransactionType
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.utils.TransactionAppConverter
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.TransactionCallback
import wacode.yamada.yuki.nempaymentapp.view.adapter.TransactionAdapter
import wacode.yamada.yuki.nempaymentapp.viewmodel.TransactionListViewModel
import javax.inject.Inject


class TransactionListFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var transactionListViewModel: TransactionListViewModel
    private val adapter = TransactionAdapter()
    private val compositeDisposable = CompositeDisposable()

    override fun layoutRes() = R.layout.fragment_transaction_list

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        transactionListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setupRecyclerView()
        setupSwipeRefreshView()

        if (adapter.itemCount == adapter.HEADER_SIZE) {
            addAllTransaction()
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    override fun onDetach() {
        super.onDetach()
        unSubscribe()
    }

    private fun setupSwipeRefreshView() {
        swipeRefreshLayout.setOnRefreshListener {
            adapter.clearItems()
            addAllTransaction()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter.onClickHandlers = {
            val listener = this@TransactionListFragment.activity as TransactionCallback
            listener.onReplaceTransactionDetail(it)
        }

        adapter.setDateChangeListener(object : TransactionAdapter.OnDateChangeListener {
            override fun showDateLabel(date: String) {
                transactionDateRootView.visibility = View.VISIBLE
                dateLabel.text = date
            }

            override fun showWalletBar() {
                transactionDateRootView.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_search, menu)
    }

    private fun addAllTransaction() {
        showProgress()
        arguments?.let {
            val wallet = it.getSerializable(KEY_WALLET_MODEL) as Wallet
            addAllTransaction(address = wallet.address)
        }
    }

    private fun addAllTransaction(address: String) {
        val accountTransfersIncoming = NemCommons.getAccountTransfersIncoming(address)
                .flatMap { Observable.fromIterable(it) }
                .map { TransactionAppConverter.convert(TransactionType.INCOMING, it) }

        val accountTransfersOutgoing = NemCommons.getAccountTransfersOutgoing(address)
                .flatMap { Observable.fromIterable(it) }
                .map { TransactionAppConverter.convert(TransactionType.OUTGOING, it) }

        val accountUnconfirmedTransactions = NemCommons.getAccountUnconfirmedTransactions(address)
                .flatMap { Observable.fromIterable(it) }
                .map { TransactionAppConverter.convert(TransactionType.UNCONFIRMED, it) }

        Observable.merge(
                accountTransfersIncoming,
                accountTransfersOutgoing,
                accountUnconfirmedTransactions)
                .toList()
                .flatMapObservable {
                    if (it.isEmpty()) {
                        throw IllegalArgumentException(EXCEPTION_LIST_EMPTY)
                    } else {
                        return@flatMapObservable Observable.fromIterable(it)
                                .flatMap { transactionAppEntity ->
                                    NemCommons.getAccountInfoFromPublicKey(transactionAppEntity.signer!!)
                                            .map { account ->
                                                transactionAppEntity.senderAddress = convertSenderAddress(transactionAppEntity, account)
                                                return@map transactionAppEntity
                                            }
                                }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    hideProgress()
                    transactionEmptyView.visibility = View.GONE
                    adapter.addItem(it)
                }, {
                    hideProgress()
                    if ((it as IllegalArgumentException).message == EXCEPTION_LIST_EMPTY) {
                        transactionEmptyView.visibility = View.VISIBLE
                    }
                    it.printStackTrace()
                }).let { compositeDisposable.add(it) }
    }

    private fun unSubscribe() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun convertSenderAddress(item: TransactionAppEntity, account: AccountMetaDataPair): String? {
        if (item.isMultisig) {
            return if (account.meta.cosignatoryOf.isNotEmpty()) {
                account.meta.cosignatoryOf[0].address.toDisplayAddress()
            } else {
                null
            }
        } else {
            return account.account.address.toDisplayAddress()
        }
    }

    companion object {
        private const val KEY_WALLET_MODEL = "key_wallet_model"
        private const val EXCEPTION_LIST_EMPTY = "list is empty"

        fun newInstance(wallet: Wallet): TransactionListFragment {
            val fragment = TransactionListFragment()
            val args = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.transaction_list_activity_title)
                putSerializable(KEY_WALLET_MODEL, wallet)
            }

            fragment.arguments = args
            return fragment
        }
    }
}