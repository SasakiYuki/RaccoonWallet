package wacode.yamada.yuki.nempaymentapp.view.fragment.profile

import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class MyProfileInfoFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_my_profile_info

    companion object {
        fun newInstance(): MyProfileInfoFragment {
            return MyProfileInfoFragment().apply {
                val args = Bundle()
                args.putInt(ARG_CONTENTS_NAME_ID, R.string.my_address_profile_activity_tab_profile)
                arguments = args
            }
        }
    }
}
