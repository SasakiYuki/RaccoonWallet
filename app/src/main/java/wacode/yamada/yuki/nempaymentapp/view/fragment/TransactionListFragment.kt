package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.TransactionCallback
import wacode.yamada.yuki.nempaymentapp.view.adapter.TransactionAdapter
import wacode.yamada.yuki.nempaymentapp.view.paging.TransactionPagingListener
import wacode.yamada.yuki.nempaymentapp.viewmodel.TransactionListViewModel
import javax.inject.Inject


class TransactionListFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: TransactionListViewModel
    private val adapter = TransactionAdapter()

    private val wallet by lazy {
        arguments?.getSerializable(KEY_WALLET_MODEL) as Wallet
    }

    override fun layoutRes() = R.layout.fragment_transaction_list

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setupRecyclerView()
        setupSwipeRefreshView()
        setupViewModelObserve()

        if (adapter.itemCount == TransactionAdapter.HEADER_SIZE) {
            transactionEmptyView.visibility = View.VISIBLE
            viewModel.getInitialLoading(wallet.address)
        }
    }

    private fun setupViewModelObserve() {
        viewModel.run {
            transactionLiveData.observe(this@TransactionListFragment, Observer {
                it ?: return@Observer

                transactionRecyclerView.tag = false
                transactionEmptyView.visibility = View.GONE
                adapter.addItem(it)
            })

            loadingStatus.observe(this@TransactionListFragment, Observer {
                it ?: return@Observer
                if (it) showProgress() else hideProgress()
            })
        }
    }

    private fun setupSwipeRefreshView() {
        swipeRefreshLayout.setOnRefreshListener {
            transactionRecyclerView.tag = true
            swipeRefreshLayout.isRefreshing = false
            transactionEmptyView.visibility = View.VISIBLE

            adapter.clearItems()
            viewModel.getInitialLoading(wallet.address)
        }
    }

    private fun setupRecyclerView() {
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(context)
        transactionRecyclerView.tag = false

        transactionRecyclerView.addOnScrollListener(object : TransactionPagingListener(transactionRecyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore() {
                viewModel.getLoadMore(wallet.address, adapter.getLastTransactionId())
            }
        })

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