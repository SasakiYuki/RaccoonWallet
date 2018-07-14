package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import com.ryuta46.nemkotlin.account.AccountGenerator
import com.ryuta46.nemkotlin.enums.Version
import com.ryuta46.nemkotlin.util.ConvertUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_import_wallet_name.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.isNotTextEmptyObservable
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.ImportWalletCallback


class ImportWalletNameFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_import_wallet_name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        walletNameEditText.isNotTextEmptyObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ item ->
                    submitButton.alpha = if (item) 1.0f else 0.5f
                    submitButton.isEnabled = item
                })

        submitButton.setOnClickListener {
            async(UI) {
                showProgress()
                bg {
                    val account = AccountGenerator.fromSeed(ConvertUtils.swapByteArray(ConvertUtils.toByteArray(secretKey.toString(Charsets.UTF_8))), Version.Main)
                    WalletManager.save(submitButton.context, account, walletNameEditText.text.toString())
                }.await()

                hideProgress()
                (this@ImportWalletNameFragment.context as ImportWalletCallback).navigateCompleteImportWallet()
            }
        }
    }

    private val secretKey by lazy {
        arguments?.getByteArray(KEY_SECRET_KEY) ?: "".toByteArray()
    }

    companion object {
        const val KEY_SECRET_KEY = "key_secret_key"
        fun newInstance(key: ByteArray): ImportWalletNameFragment {
            val fragment = ImportWalletNameFragment()
            val args = Bundle().apply {
                putByteArray(KEY_SECRET_KEY, key)
                putInt(ARG_CONTENTS_NAME_ID, R.string.import_wallet_name_fragment_title)
            }
            fragment.arguments = args
            return fragment
        }
    }
}