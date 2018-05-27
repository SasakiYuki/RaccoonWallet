package wacode.yamada.yuki.nempaymentapp.view.fragment.top

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_qr_lab.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.view.activity.CalculatorActivity
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class QRLabFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_qr_lab

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupQrLabMainButton()
    }

    private fun setupQrLabMainButton() {
        qrGeneratorIntentButton.setOnClickListener {
            startActivity(CalculatorActivity.createIntent(context))
        }

        sellListButton.setOnClickListener { context.showToast(R.string.com_coming_soon) }

        qrButton.setOnClickListener { context.showToast(R.string.com_coming_soon) }
    }

    companion object {
        fun newInstance(): QRLabFragment {
            val fragment = QRLabFragment()
            return fragment
        }
    }
}