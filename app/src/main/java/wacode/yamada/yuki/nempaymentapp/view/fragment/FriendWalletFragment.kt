package wacode.yamada.yuki.nempaymentapp.view.fragment


import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.R


class FriendWalletFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_create_friend_wallet

    companion object {
        fun newInstance(): FriendWalletFragment {
            val fragment = FriendWalletFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.address_book_friend_wallet_title)
            fragment.arguments = args
            return fragment
        }
    }
}