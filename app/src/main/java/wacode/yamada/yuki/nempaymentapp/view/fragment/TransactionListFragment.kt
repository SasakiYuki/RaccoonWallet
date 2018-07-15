package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.ryuta46.nemkotlin.model.AccountMetaDataPair
import com.ryuta46.nemkotlin.model.TransactionMetaDataPair
import com.ryuta46.nemkotlin.model.UnconfirmedTransactionMetaDataPair
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.toDisplayAddress
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.types.TransactionType
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.utils.TransactionAppConverter
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.TransactionCallback
import wacode.yamada.yuki.nempaymentapp.view.adapter.TransactionAdapter


class TransactionListFragment : BaseFragment() {

    private val adapter = TransactionAdapter()
    private val compositeDisposable = CompositeDisposable()

    override fun layoutRes() = R.layout.fragment_transaction_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setupRecyclerView()
        setupSwipeRefreshView()

        if (adapter.itemCount == adapter.HEADER_SIZE) {
            addAllTransaction()
        }
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
                .singleOrError()

        val accountTransfersOutgoing = NemCommons.getAccountTransfersOutgoing(address)
                .singleOrError()

        val accountUnconfirmedTransactions = NemCommons.getAccountUnconfirmedTransactions(address)
                .singleOrError()

        compositeDisposable.add(
                Single.zip(
                        accountTransfersIncoming,
                        accountTransfersOutgoing,
                        accountUnconfirmedTransactions,
                        Function3<List<TransactionMetaDataPair>, List<TransactionMetaDataPair>, List<UnconfirmedTransactionMetaDataPair>, List<TransactionAppEntity>> { incoming, outgoing, unconfirmed ->
                            val list = Observable.fromIterable(incoming)
                                    .map { TransactionAppConverter.convert(TransactionType.INCOMING, it) }
                                    .toList()
                                    .blockingGet()

                            list.addAll(Observable.fromIterable(outgoing)
                                    .map { TransactionAppConverter.convert(TransactionType.OUTGOING, it) }
                                    .toList()
                                    .blockingGet())

                            list.addAll(Observable.fromIterable(unconfirmed)
                                    .map { TransactionAppConverter.convert(TransactionType.UNCONFIRMED, it) }
                                    .toList()
                                    .blockingGet())

                            list
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.isEmpty()) {
                                hideProgress()
                                transactionEmptyView.visibility = View.VISIBLE
                            } else {
                                transactionEmptyView.visibility = View.GONE
                                fetchSenderAddress(it)
                            }
                        }, {
                            commonErrorHandling(it)
                        })
        )
    }

    private fun fetchSenderAddress(list: List<TransactionAppEntity>) {
        compositeDisposable.add(
                Observable.fromIterable(list)
                        .subscribe({ response ->
                            NemCommons.getAccountInfoFromPublicKey(response.signer!!)
                                    .subscribe({ account ->
                                        response.senderAddress = convertSenderAddress(response, account)
                                        adapter.addItem(response)
                                        adapter.notifyDataSetChanged()
                                        hideProgress()
                                    }, {
                                        commonErrorHandling(it)
                                    })
                        }, {
                            commonErrorHandling(it)
                        })
        )
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

    private fun commonErrorHandling(e: Throwable) {
        hideProgress()
        e.printStackTrace()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    companion object {
        private const val KEY_WALLET_MODEL = "key_wallet_model"

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