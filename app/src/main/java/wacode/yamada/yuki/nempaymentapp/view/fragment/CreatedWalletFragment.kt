package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import com.ryuta46.nemkotlin.account.Account
import com.ryuta46.nemkotlin.account.AccountGenerator
import com.ryuta46.nemkotlin.enums.Version
import kotlinx.android.synthetic.main.fragment_created_wallet.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.toDisplayAddress
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.CreateWalletDisplayType
import wacode.yamada.yuki.nempaymentapp.view.activity.OnCreateWalletPageChangeListener

class CreatedWalletFragment : BaseFragment() {
    private lateinit var listener: OnCreateWalletPageChangeListener
    private lateinit var account: Account

    override fun layoutRes() = R.layout.fragment_created_wallet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress()
        CoroutineScope(Dispatchers.Main).launch {
            async(Dispatchers.IO) {
                account = createWallet()
                arguments?.let {
                    WalletManager.save(view.context, account, it.getString(KEY_WALLET_NAME))
                }
            }.await()
            showWalletAddress(account)
            hideProgress()
        }
        setupButton()
    }

    private fun setupButton() {
        button.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                var count = 0
                count = async(Dispatchers.IO) {
                    NemPaymentApplication.database.walletDao().getSize()
                }.await()

                if (count == 0) {
                    listener.onClickNextButton(CreateWalletDisplayType.CREATED)
                } else {
                    listener.onClickNextButton(CreateWalletDisplayType.CREATED_ALREADY)
                }
            }
        }
    }

    private fun showWalletAddress(account: Account) {
        publicAddressTextView.text = account.address.toDisplayAddress()
    }

    private fun createWallet() = AccountGenerator.fromRandomSeed(Version.Main)

    companion object {
        private const val KEY_WALLET_NAME = "wallet_name"
        fun newInstance(walletName: String, listener: OnCreateWalletPageChangeListener): CreatedWalletFragment {
            val fragment = CreatedWalletFragment()
            val bundle = Bundle()
            bundle.putString(KEY_WALLET_NAME, walletName)
            bundle.putInt(ARG_CONTENTS_NAME_ID, R.string.created_wallet_activity_title)
            fragment.arguments = bundle
            fragment.listener = listener
            return fragment
        }
    }
}