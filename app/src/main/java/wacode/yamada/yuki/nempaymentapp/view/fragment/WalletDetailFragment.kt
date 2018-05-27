package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import com.ryuta46.nemkotlin.model.AccountMetaDataPair
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_wallet_detail.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons

class WalletDetailFragment : BaseFragment() {
    private val compositeDisposable = CompositeDisposable()

    override fun layoutRes() = R.layout.fragment_wallet_detail

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress()
        fetchWalletDetailFromDb()
    }

    private fun fetchWalletDetailFromDb() {
        var wallet: Wallet? = null
        async(UI) {
            bg {
                wallet = NemPaymentApplication.database.walletDao().getById(getWalletId)
            }.await()
            fetchWalletDetailFromApi(wallet)
        }
    }

    private fun fetchWalletDetailFromApi(wallet: Wallet?) {
        wallet?.let {
            compositeDisposable.add(NemCommons.getAccountInfo(it.address)
                    .subscribe({
                        setupViews(it)
                    }, { e ->
                        hideProgress()
                        e.printStackTrace()
                    }))
        }
    }

    private fun setupViews(data: AccountMetaDataPair) {
        walletType.text = if (data.meta.cosignatories.isNotEmpty()) {
            getString(R.string.wallet_setting_type_multisig)
        } else {
            getString(R.string.wallet_setting_type_standard)
        }

        val account = data.account
        walletConstitution.text = account.multisigInfo.minCosignatories.toString() + " of " + account.multisigInfo.cosignatoriesCount.toShort()
        walletScore.text = account.importance.toString()
        walletPublicKey.text = if (account.publicKey.isNullOrEmpty()) {
            getString(R.string.wallet_detail_public_key_empty)
        } else {
            account.publicKey
        }

        hideProgress()
    }

    private val getWalletId by lazy {
        arguments.getLong(KEY_WALLET_ID)
    }

    companion object {
        private const val KEY_WALLET_ID = "key_wallet_id"
        fun newInstance(id: Long): WalletDetailFragment {
            val fragment = WalletDetailFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.wallet_detail_fragment_title)
            args.putLong(KEY_WALLET_ID, id)
            fragment.arguments = args
            return fragment
        }
    }
}