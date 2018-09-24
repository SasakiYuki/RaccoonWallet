package wacode.yamada.yuki.nempaymentapp.view.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_friend_wallet.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.view.activity.OnFriendDataChangeCallback
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletInfoListController
import wacode.yamada.yuki.nempaymentapp.viewmodel.FriendWalletViewModel
import javax.inject.Inject


class FriendWalletFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var friendWalletViewModel: FriendWalletViewModel
    var walletList = ArrayList<WalletInfo>()
    private lateinit var controller: WalletInfoListController

    private val friendId by lazy {
        arguments?.getLong(PARAMS_FRIEND_ID)
    }

    override fun layoutRes() = R.layout.fragment_friend_wallet

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        friendWalletViewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendWalletViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModelObserve()

        friendWalletViewModel.queryWalletInfo(friendId!!)
    }

    private fun setupViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        controller = WalletInfoListController()
        recyclerView.adapter = controller.adapter
    }

    private fun setupViewModelObserve() {
        friendWalletViewModel.run {
            loadingStatus.observe(this@FriendWalletFragment, Observer {
                it ?: return@Observer

                if (!it) {
                    hideEmptyView()
                }
            })

            walletInfoLiveData.observe(this@FriendWalletFragment, Observer {
                it ?: return@Observer

                addListItem(it)
            })
        }
    }

    private fun addListItem(walletInfo: WalletInfo) {
        walletList.add(walletInfo)
        controller.setData(walletList)

        if (activity is OnFriendDataChangeCallback) {
            (activity as OnFriendDataChangeCallback).onFriendWalletChanged(walletList.size)
        }
    }

    private fun hideEmptyView() {
        emptyRootView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    fun onListItemChanged(walletInfo: WalletInfo) {
        addListItem(walletInfo)

        friendWalletViewModel.insertFriendAddress(friendId!!, walletInfo)
    }

    companion object {
        const val PAGE_POSITION = 0
        private const val PARAMS_FRIEND_ID = "params_friend_id"

        fun newInstance(friendId: Long): FriendWalletFragment {
            val fragment = FriendWalletFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.address_book_friend_wallet_title)
            args.putLong(PARAMS_FRIEND_ID, friendId)
            fragment.arguments = args
            return fragment
        }
    }
}