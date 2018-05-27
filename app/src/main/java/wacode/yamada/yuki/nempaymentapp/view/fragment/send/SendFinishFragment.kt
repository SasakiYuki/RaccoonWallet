package wacode.yamada.yuki.nempaymentapp.view.fragment.send

import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_send_finish.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.MainActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.TransactionActivity
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class SendFinishFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_send_finish

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    private fun setupButtons() {
        homeButton.setOnClickListener {
            val intent = MainActivity.createIntent(context, false)
            intent.flags = FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        transactionButton.setOnClickListener {
            async(UI) {
                val wallet = bg { WalletManager.getSelectedWallet(getContext()) }.await()
                wallet?.let {
                    startActivity(TransactionActivity.getCallingIntent(getContext(),wallet))
                }
                finish()
            }
        }
    }

    companion object {
        fun newInstance(): SendFinishFragment {
            val fragment = SendFinishFragment()
            val arguments = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.send_finish_fragment_title)
            }
            fragment.arguments = arguments
            return fragment
        }
    }
}