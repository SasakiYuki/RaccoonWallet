package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.R

class CreateFriendWalletFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_friend_wallet

    companion object {
        const val PAGE_POSITION = 0

        fun newInstance(): CreateFriendWalletFragment {
            val fragment = CreateFriendWalletFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.create_friend_wallet_title)
            fragment.arguments = args
            return fragment
        }
    }
}