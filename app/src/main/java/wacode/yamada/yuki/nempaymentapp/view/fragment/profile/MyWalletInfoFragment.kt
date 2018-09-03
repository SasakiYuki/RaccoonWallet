package wacode.yamada.yuki.nempaymentapp.view.fragment.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_my_wallet_info.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletInfoListController
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
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
            walletInfoLiveData.observe(this@MyWalletInfoFragment, Observer {
                it ?: return@Observer
                controller.setData(myWalletInfoViewModel.walletInfoItems)
            })
        }
    }

    private fun setupViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        controller = WalletInfoListController()
        recyclerView.adapter = controller.adapter
    }

    companion object {
        fun newInstance(): MyWalletInfoFragment {
            return MyWalletInfoFragment().apply {
                val args = Bundle()
                args.putInt(ARG_CONTENTS_NAME_ID, R.string.my_address_profile_activity_tab_profile)
                arguments = args
            }
        }
    }
}