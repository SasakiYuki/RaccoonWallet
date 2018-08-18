package wacode.yamada.yuki.nempaymentapp.view.fragment.donate

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_raccoon_donate.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.activity.drawer.RaccoonDonateActivity
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class DonateTopFragment : BaseFragment() {
    private lateinit var viewModel: RaccoonDonateActivity.RaccoonDonateViewModel
    override fun layoutRes() = R.layout.fragment_raccoon_donate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
    }

    private fun setupButtons() {
        androidButton.setOnClickListener {
            viewModel.donateType = RaccoonDonateActivity.DonateType.ANDROID
            viewModel.replace(RaccoonDonateActivity.DonateDisplayType.DETAIL)
        }
        rhimeButton.setOnClickListener {
            viewModel.donateType = RaccoonDonateActivity.DonateType.RHIME
            viewModel.replace(RaccoonDonateActivity.DonateDisplayType.DETAIL)
        }
        iosButton.setOnClickListener {
            viewModel.donateType = RaccoonDonateActivity.DonateType.IOS
            viewModel.replace(RaccoonDonateActivity.DonateDisplayType.DETAIL)
        }
    }

    companion object {
        fun newInstance(viewModel: RaccoonDonateActivity.RaccoonDonateViewModel): DonateTopFragment {
            val fragment = DonateTopFragment()
            val args = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.raccoon_donate_activity_title)
            }
            fragment.arguments = args
            fragment.viewModel = viewModel
            return fragment
        }
    }
}