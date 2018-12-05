package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.text.InputFilter
import android.view.View
import com.ryuta46.nemkotlin.account.AccountGenerator
import com.ryuta46.nemkotlin.enums.Version
import com.ryuta46.nemkotlin.util.ConvertUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_import_secret_key.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.isNotTextEmptyObservable
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.extentions.toHexByteArray
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.ImportWalletCallback


class ImportSecretKeyFragment : BaseFragment() {
    private lateinit var callback: ImportWalletCallback

    override fun layoutRes() = R.layout.fragment_import_secret_key

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback = context as ImportWalletCallback
        setupButtons()
        setupEditText()
    }

    private fun setupButtons() {
        policyButton.setOnClickListener {
            val url = "https://raccoonwallet.com/tos_pp/"
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(activity, Uri.parse(url))
        }

        scanIconButton.setOnClickListener {
            replaceFragment(QrScanFragment.newInstance(true, true), true)
        }

        nextButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                showProgress()
                try {
                    val rawKey = secretKeyEditText.text.toString()
                    val privateKey = if (rawKey.length == 66 && rawKey.startsWith("00")) {
                        rawKey.substring(2, 66)
                    } else {
                        rawKey
                    }

                    async(Dispatchers.IO) {
                        AccountGenerator.fromSeed(ConvertUtils.swapByteArray(privateKey.toHexByteArray()), Version.Main)
                    }.await()
                    hideProgress()
                    callback.onReplaceImportWalletName(privateKey.toByteArray(Charsets.UTF_8))
                } catch (e: Exception) {
                    hideProgress()
                    this@ImportSecretKeyFragment.nextButton.context.showToast(R.string.import_secret_key_input_error)
                }
            }
        }
    }

    private fun setupEditText() {
        secretKeyEditText.isNotTextEmptyObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ item ->
                    nextButton.alpha = if (item) 1.0f else 0.5f
                    nextButton.isEnabled = item
                })


        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            if (source.toString().matches("^[a-zA-Z0-9]+$".toRegex())) {
                source
            } else ""
        }

        val filters = arrayOf(inputFilter)
        secretKeyEditText.filters = filters
    }

    companion object {
        fun newInstance(): ImportSecretKeyFragment {
            val fragment = ImportSecretKeyFragment()
            val args = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.import_secret_key_fragment_title)
            }
            fragment.arguments = args
            return fragment
        }
    }
}