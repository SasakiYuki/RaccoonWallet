package wacode.yamada.yuki.nempaymentapp.view.fragment.donate

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_donate_detail.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.view.activity.SendActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.drawer.RaccoonDonateActivity
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class DonateDetailFragment : BaseFragment() {
    private lateinit var viewModel: RaccoonDonateActivity.RaccoonDonateViewModel
    private val compositeDisposable = CompositeDisposable()
    override fun layoutRes() = R.layout.fragment_donate_detail

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupButton()
    }

    private fun setupButton() {
        button.setOnClickListener {
            checkEnterAddressAvailable()
        }
    }

    private fun checkEnterAddressAvailable() {
        val address = when (viewModel.donateType) {
            RaccoonDonateActivity.DonateType.ANDROID -> ADDRESS_ANDROID
            RaccoonDonateActivity.DonateType.IOS -> ADDRESS_IOS
            RaccoonDonateActivity.DonateType.RHIME -> ADDRESS_RHIME
        }
        showProgress()
        compositeDisposable.add(
                NemCommons.getAccountInfo(address)
                        .subscribe({ item ->
                            startActivity(SendActivity.createIntent(context, address, item.account.publicKey))
                            hideProgress()
                        }, { e ->
                            hideProgress()
                            context.showToast(R.string.send_top_fragment_address_error)
                        }))
    }

    private fun setupViews() {
        when (viewModel.donateType) {
            RaccoonDonateActivity.DonateType.ANDROID -> setupAndroidMode()
            RaccoonDonateActivity.DonateType.RHIME -> setupRhimeMode()
            RaccoonDonateActivity.DonateType.IOS -> setupRyutaMode()
        }
    }

    private fun setupAndroidMode() {
        imageView.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.icon_yuki))
        nameTextView.text = getString(R.string.raccoon_donate_activity_android)
        subTitleTextView.text = getString(R.string.raccoon_donate_activity_engineer)
        donateMainTextView.text = getString(R.string.donate_detail_fragment_android_message)
    }

    private fun setupRhimeMode() {
        imageView.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.icon_rhime))
        nameTextView.text = getString(R.string.raccoon_donate_activity_rhime)
        subTitleTextView.text = getString(R.string.raccoon_donate_activity_ui_design)
        donateMainTextView.text = getString(R.string.donate_detail_fragment_rhime_message)
    }

    private fun setupRyutaMode() {
        imageView.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.icon_ryuta))
        nameTextView.text = getString(R.string.raccoon_donate_activity_ios)
        subTitleTextView.text = getString(R.string.raccoon_donate_activity_engineer)
        donateMainTextView.text = getString(R.string.donate_detail_fragment_ios_message)
    }

    companion object {
        private const val ADDRESS_ANDROID = "NBHF3BSD4OHRIXHIERML27LHABVKK2MVK36YOYUN"
        private const val ADDRESS_IOS = "NAEZYI6YPR4YIRN4EAWSP3GEYU6ATIXKTXSVBEU5"
        private const val ADDRESS_RHIME = "NA4JR3MMBGS2P5U6WD7WVKYE5IHJZYICDDUL3IQI"
        fun newInstance(viewModel: RaccoonDonateActivity.RaccoonDonateViewModel): DonateDetailFragment {
            val fragment = DonateDetailFragment()
            val args = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.donate_detail_fragment_title)
            }
            fragment.arguments = args
            fragment.viewModel = viewModel
            return fragment
        }
    }
}