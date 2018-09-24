package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.view.View
import wacode.yamada.yuki.nempaymentapp.R

class TwitterAuthBottomSheetDialogFragment : FullScreenBottomSheet() {
    override fun viewToInflate() = R.layout.fragment_twitter_auth_bottom_sheet_dialog
    override fun onViewInflated(view: View) {
    }

    companion object {
        fun newInstance(): TwitterAuthBottomSheetDialogFragment {
            return TwitterAuthBottomSheetDialogFragment()
        }
    }
}

