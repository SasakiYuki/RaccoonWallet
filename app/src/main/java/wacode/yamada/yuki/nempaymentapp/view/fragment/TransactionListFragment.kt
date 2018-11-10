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
            viewModel.getInitialLoading("NAKK6BAUEI7YLELPM3EYVQT6U3JL5LLM6IK5XBCE")
        }
    }

    private fun setupViewModelObserve() {
        viewModel.run {
            transactionLiveData.observe(this@TransactionListFragment, Observer {
                it ?: return@Observer

                adapter.addItem(it)
            })

            loadingStatusLiveData.observe(this@TransactionListFragment, Observer {
                it ?: return@Observer

                when (it) {
                    TransactionListViewModel.LoadingStatus.INITIAL_LOADING -> {
                        swipeRefreshLayout.isRefreshing = false
                        transactionRecyclerView.setTag(R.id.allow_loading, false)
                        adapter.showIndicator(true)
                    }
                    TransactionListViewModel.LoadingStatus.MORE_LOADING -> {
                        adapter.showIndicator(true)
                        transactionRecyclerView.setTag(R.id.allow_loading, false)
                    }
                    TransactionListViewModel.LoadingStatus.SUCCESS -> {
                        transactionRecyclerView.setTag(R.id.allow_loading, true)
                    }
                    TransactionListViewModel.LoadingStatus.EMPTY -> {
                        adapter.showIndicator(false)
                        transactionEmptyView.visibility = View.VISIBLE
                        transactionRecyclerView.setTag(R.id.allow_loading, false)
                    }
                    TransactionListViewModel.LoadingStatus.COMPLETE -> {
                        adapter.showIndicator(false)
                        transactionRecyclerView.setTag(R.id.allow_loading, false)
                    }
                }
            })
        }
    }

    private fun setupSwipeRefreshView() {
        swipeRefreshLayout.setOnRefreshListener {
            adapter.clearItems()
            viewModel.getInitialLoading("NAKK6BAUEI7YLELPM3EYVQT6U3JL5LLM6IK5XBCE")
        }
    }

    private fun setupRecyclerView() {
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(context)

        transactionRecyclerView.addOnScrollListener(object : TransactionPagingListener(transactionRecyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore() {
                viewModel.getLoadMore("NAKK6BAUEI7YLELPM3EYVQT6U3JL5LLM6IK5XBCE", adapter.getLastTransactionId())
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