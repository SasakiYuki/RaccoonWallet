package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_private_key_display.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager

class WalletBackupFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_private_key_display

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        showProgress()

        arguments?.let {
            val walletId = it.getLong(KEY_WALLET_ID)
            async(UI) {
                val wallet = bg { NemPaymentApplication.database.walletDao().getById(walletId) }
                        .await()
                textView.text = WalletManager.getPrivateKey(textView.context, wallet)

                hideProgress()
            }
        }

        textView.setOnClickListener {
            val text = textView.text.toString()
            copyToClip(text)
            textView.context.showToast(R.string.private_key_display_fragment_copied_click)
        }

        button.setOnClickListener {
            finish()
        }
    }

    private fun copyToClip(text: String) {
        //TODO extentionsにある
        context?.let {
            val item = ClipData.Item(text)

            val mimeType = arrayOfNulls<String>(1)
            mimeType[0] = ClipDescription.MIMETYPE_TEXT_URILIST

            val cd = ClipData(ClipDescription("text_data", mimeType), item)

            val cm = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.primaryClip = cd
        }
    }

    companion object {
        private const val KEY_WALLET_ID = "key_wallet_id"

        fun newInstance(id: Long): WalletBackupFragment {
            val fragment = WalletBackupFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.wallet_backup_activity_title)
            args.putLong(KEY_WALLET_ID, id)
            fragment.arguments = args
            return fragment
        }
    }
}