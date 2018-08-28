package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_private_key_display.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.utils.WalletProvider
import wacode.yamada.yuki.nempaymentapp.view.activity.OnPrivateKeyStorePageChangeListener
import wacode.yamada.yuki.nempaymentapp.view.activity.TutorialPinCodeActivity


class PrivateKeyDisplayFragment : BaseFragment() {
    private var listenerPrivateKeyStore: OnPrivateKeyStorePageChangeListener? = null
    override fun layoutRes() = R.layout.fragment_private_key_display

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextView()
        setupButton()
    }

    private fun setupButton() {
        button.setOnClickListener {
            startActivity(TutorialPinCodeActivity.createIntent(button.context))
            finish()
        }
    }

    private fun setupTextView() {
        WalletProvider.wallet?.let {
            textView.text = WalletManager.getPrivateKey(textView.context, it)
        }

        textView.setOnClickListener {
            val text = textView.text.toString()
            copyToClip(text)
            textView.context.showToast(R.string.private_key_display_fragment_copied_click)
        }
    }

    private fun copyToClip(text: String) {
        // TODO extentionsにある
        val item = ClipData.Item(text)

        val mimeType = arrayOfNulls<String>(1)
        mimeType[0] = ClipDescription.MIMETYPE_TEXT_URILIST

        val cd = ClipData(ClipDescription("text_data", mimeType), item)

        val cm = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        cm.primaryClip = cd
    }

    companion object {
        fun newInstance(listenerPrivateKeyStore: OnPrivateKeyStorePageChangeListener): PrivateKeyDisplayFragment {
            val fragment = PrivateKeyDisplayFragment()
            fragment.listenerPrivateKeyStore = listenerPrivateKeyStore
            val bundle = Bundle()
            bundle.putInt(ARG_CONTENTS_NAME_ID, R.string.private_key_display_fragment_title)
            fragment.arguments = bundle
            return fragment
        }
    }
}
