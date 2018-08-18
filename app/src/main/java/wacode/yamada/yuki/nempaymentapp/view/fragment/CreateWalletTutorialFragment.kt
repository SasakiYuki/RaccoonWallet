package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_create_wallet_tutorial.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.viewmodel.ChooseCreateOrScanWalletViewModel

class CreateWalletTutorialFragment : BaseFragment() {
    private lateinit var viewModel: ChooseCreateOrScanWalletViewModel
    override fun layoutRes() = R.layout.fragment_create_wallet_tutorial

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
    }

    private fun setupButton() {
        button.setOnClickListener {
            viewModel.replaceFragment()
        }
    }

    companion object {
        fun newInstance(chooseCreateOrScanWalletViewModel: ChooseCreateOrScanWalletViewModel): CreateWalletTutorialFragment {
            val fragment = CreateWalletTutorialFragment()
            fragment.viewModel = chooseCreateOrScanWalletViewModel
            val bundle = Bundle()
            bundle.putInt(ARG_CONTENTS_NAME_ID, R.string.create_wallet_tutorial_title)
            fragment.arguments = bundle
            return fragment
        }
    }
}
