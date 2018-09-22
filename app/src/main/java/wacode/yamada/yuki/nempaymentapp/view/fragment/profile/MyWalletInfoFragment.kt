package wacode.yamada.yuki.nempaymentapp.view.fragment.profile

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_my_wallet_info.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.copyClipBoard
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.view.activity.profile.MyAddressProfileActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.profile.ProfileAddressAddActivity
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletInfoClickListener
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletInfoListController
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.BottomSheetListDialogFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.MyWalletInfoViewModel
import javax.inject.Inject

class MyWalletInfoFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var myWalletInfoViewModel: MyWalletInfoViewModel
    private lateinit var controller: WalletInfoListController
    override fun layoutRes() = R.layout.fragment_my_wallet_info

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        myWalletInfoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyWalletInfoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupViewModel()
        myWalletInfoViewModel.findAllMyAddress()
    }

    private fun setupViewModel() {
        myWalletInfoViewModel.apply {
            myAddressLiveData.observe(this@MyWalletInfoFragment, Observer {
                it ?: return@Observer
                myWalletInfoViewModel.selectWalletInfo(it.walletInfoId)
            })
            walletInfoUpdatedLiveData.observe(this@MyWalletInfoFragment, Observer {
                it ?: return@Observer
                controller.setData(myWalletInfoViewModel.walletInfoItems)
                myWalletInfoViewModel.walletInfoItems
                        .firstOrNull { it.isMaster }
                        ?.let {
                            myWalletInfoViewModel.sendBusMasterWallet(it)
                        }
            })
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

    private fun onClickCopyRow(walletInfo: WalletInfo) {
        context?.let {
            it.showToast(R.string.my_wallet_info_fragment_address_copied_toast)
            walletInfo.walletAddress.copyClipBoard(it)
        }
    }

    private fun onClickSendRow(walletInfo: WalletInfo) {
        activity?.let {
            it.setResult(Activity.RESULT_OK, Intent().putExtra(
                    MyAddressProfileActivity.RESULT_PAYMENT_ADDRESS,
                    walletInfo.walletAddress))
            finish()
        }
    }

    private fun onClickEditRow(walletInfo: WalletInfo) {
        activity?.let {
            startActivity(ProfileAddressAddActivity.createIntent(it,
                    ProfileAddressAddActivity.ProfileAddressAddType.Edit,
                    walletInfo))
        }
    }

    private fun onClickDeleteRow(walletInfo: WalletInfo) {
        myWalletInfoViewModel.deleteMyAddress(walletInfo)
    }

    companion object {
        fun newInstance(): MyWalletInfoFragment {
            return MyWalletInfoFragment().apply {
                val args = Bundle()
                args.putInt(ARG_CONTENTS_NAME_ID, R.string.my_address_profile_activity_tab_wallet)
                arguments = args
            }
        }
    }
}