package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_private_key_description.*
import wacode.yamada.yuki.nempaymentapp.R

class WalletBackupWarningFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_private_key_description

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            replaceFragment(WalletBackupFragment.newInstance(getWalletId), true)
        }
    }

    private val getWalletId by lazy {
        arguments.getLong(KEY_WALLET_ID)
    }

    companion object {
        private const val KEY_WALLET_ID = "key_wallet_id"

        fun newInstance(id: Long): WalletBackupWarningFragment {
            val fragment = WalletBackupWarningFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.wallet_backup_activity_title)
            args.putLong(KEY_WALLET_ID, id)
            fragment.arguments = args
            return fragment
        }
    }
}