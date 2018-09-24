package wacode.yamada.yuki.nempaymentapp.view.fragment


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_friend_wallet.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.copyClipBoard
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.view.activity.MainActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.OnFriendDataChangeCallback
import wacode.yamada.yuki.nempaymentapp.view.activity.profile.ProfileAddressAddActivity
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletInfoClickListener
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
        showEmptyView()

        friendWalletViewModel.queryWalletInfo(friendId!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_UPDATE_WALLET_INFO -> {
                    val item = data?.getSerializableExtra(ProfileAddressAddActivity.INTENT_WALLET_INFO) as WalletInfo
                    addListItem(item)
                }
            }
        }
    }

    private fun setupViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        controller = WalletInfoListController(object : WalletInfoClickListener {
            override fun onRowClick(walletInfo: WalletInfo) {
                val fragment = BottomSheetListDialogFragment.newInstance(getString(R.string.bottom_my_wallet_info_copy), R.menu.bottom_my_wallet_info) { fragment, itemId ->
                    when (itemId) {
                        R.id.copy -> onClickCopyRow(walletInfo)
                        R.id.send -> onClickSendRow(walletInfo)
                        R.id.edit -> onClickEditRow(walletInfo)
                        R.id.delete -> onClickDeleteRow(walletInfo)
                    }
                    fragment.dismiss()
                }
                fragment.show(activity?.supportFragmentManager, fragment.tag)
            }
        })

        recyclerView.adapter = controller.adapter
    }

    private fun setupViewModelObserve() {
        friendWalletViewModel.run {
            walletInfoLiveData.observe(this@FriendWalletFragment, Observer {
                it ?: return@Observer

                hideEmptyView()
                addListItem(it)
            })
        }
    }

    private fun onClickCopyRow(walletInfo: WalletInfo) {
        context?.let {
            it.showToast(R.string.com_copied)
            walletInfo.walletAddress.copyClipBoard(it)
        }
    }

    private fun onClickSendRow(walletInfo: WalletInfo) {
        context?.let {
            startActivity(MainActivity.createIntentAtSendFragment(it, walletInfo.walletAddress))
        }
    }

    private fun onClickEditRow(walletInfo: WalletInfo) {
        activity?.let {
            startActivityForResult(ProfileAddressAddActivity.createIntent(it,
                    ProfileAddressAddActivity.ProfileAddressAddType.Edit,
                    walletInfo), RESULT_UPDATE_WALLET_INFO)
        }
    }

    private fun onClickDeleteRow(walletInfo: WalletInfo) {
        walletList.clear()
        friendWalletViewModel.removeFriendAddressAndGetAll(friendId!!, walletInfo)
    }

    private fun addListItem(walletInfo: WalletInfo) {
        walletList = Observable.fromIterable(walletList)
                .filter { it.id != walletInfo.id }
                .toList()
                .blockingGet() as ArrayList<WalletInfo>

        walletList.add(walletInfo)

        controller.setData(walletList)

        if (activity is OnFriendDataChangeCallback) {
            (activity as OnFriendDataChangeCallback).onFriendWalletChanged(walletList.size)
        }
    }

    private fun showEmptyView() {
        emptyRootView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
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
        private const val RESULT_UPDATE_WALLET_INFO = 1001

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