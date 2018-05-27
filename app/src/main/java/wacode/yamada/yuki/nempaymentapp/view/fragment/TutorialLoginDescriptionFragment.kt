package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_login_description.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.view.activity.OnPrivateKeyStorePageChangeListener
import wacode.yamada.yuki.nempaymentapp.view.activity.PrivateKeyStoreSupportDisplayType
import wacode.yamada.yuki.nempaymentapp.view.activity.WalletCreatedActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonSelectDialog
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonSelectViewModel
import wacode.yamada.yuki.nempaymentapp.view.dialog.SelectDialogButton

class TutorialLoginDescriptionFragment : BaseFragment() {
    private lateinit var listenerPrivateKeyStore: OnPrivateKeyStorePageChangeListener
    override fun layoutRes() = R.layout.fragment_login_description

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
    }

    private fun setupButton() {
        button.setOnClickListener {
            if (PinCodeHelper.isAvailable(context)) {
                val viewModel = RaccoonSelectViewModel(getString(R.string.com_ok), getString(R.string.com_cancel))

                viewModel.clickEvent
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { item ->
                            if (item.equals(SelectDialogButton.POSITIVE)) {
                                startActivity(WalletCreatedActivity.createIntent(context, WalletCreatedType.LOGIN))
                            }
                        }

                RaccoonSelectDialog.createDialog(viewModel,
                        getString(R.string.create_wallet_tutorial_dialog_title),
                        getString(R.string.create_wallet_tutorial_dialog_message))
                        .show(activity.supportFragmentManager, "")
            } else {
                listenerPrivateKeyStore.onClickNextButton(PrivateKeyStoreSupportDisplayType.LOGIN_DESCRIPTION)
            }
        }
    }

    companion object {
        fun newInstance(listenerPrivateKeyStore: OnPrivateKeyStorePageChangeListener): TutorialLoginDescriptionFragment {
            val fragment = TutorialLoginDescriptionFragment()
            fragment.listenerPrivateKeyStore = listenerPrivateKeyStore
            val bundle = Bundle()
            bundle.putInt(ARG_CONTENTS_NAME_ID, R.string.tutorial_login_description_fragment_title)
            fragment.arguments = bundle
            return fragment
        }
    }
}