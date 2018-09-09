package wacode.yamada.yuki.nempaymentapp.view.fragment.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.View
import dagger.android.support.AndroidSupportInjection
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.MyProfileInfoViewModel
import kotlinx.android.synthetic.main.fragment_my_profile_info.*
import javax.inject.Inject

class MyProfileInfoFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var myProfileInfoViewModel: MyProfileInfoViewModel
    override fun layoutRes() = R.layout.fragment_my_profile_info

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        myProfileInfoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyProfileInfoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupViewModel()
    }

    private fun setupViews() {
        myProfileInfoViewModel.onInit()
    }

    private fun setupViewModel() {
        myProfileInfoViewModel.myAddressCountLiveData
                .observe(this, Observer {
                    it ?: return@Observer
                    walletCountTextView.text = it.toString()
                })
    }

    companion object {
        fun newInstance(): MyProfileInfoFragment {
            return MyProfileInfoFragment().apply {
                val args = Bundle()
                args.putInt(ARG_CONTENTS_NAME_ID, R.string.my_address_profile_activity_tab_profile)
                arguments = args
            }
        }
    }
}