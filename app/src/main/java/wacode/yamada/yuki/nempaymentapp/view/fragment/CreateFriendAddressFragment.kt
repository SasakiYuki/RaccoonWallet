package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.R

class CreateFriendAddressFragment : BaseFragment() {
    override fun layoutRes()= R.layout.fragment_create_friend_address

    companion object {
        fun newInstance(): CreateFriendAddressFragment {
            val fragment = CreateFriendAddressFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.create_friend_address_title)
            fragment.arguments = args
            return fragment
        }
    }
}