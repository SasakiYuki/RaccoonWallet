package wacode.yamada.yuki.nempaymentapp.view.fragment.send

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.view_calculator_title.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicItem
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class CalculatorTitleFragment : BaseFragment() {
    override fun layoutRes() = R.layout.view_calculator_title

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        getMosaicCountText()
    }

    private fun getMosaicCountText() {
        val mosaicItem = getMosaicItem()
        mosaicCountText.text = if (mosaicItem.isNEMXEMItem()) getText(R.string.com_xem_uppercase) else mosaicItem.getFullName()
    }

    private fun getMosaicItem() = arguments.get(KEY_MOSAIC_ITEM) as MosaicItem

    companion object {
        private val KEY_MOSAIC_ITEM = "key_mosaic_item"
        fun newInstance(mosaicItem: MosaicItem): CalculatorTitleFragment {
            val fragment = CalculatorTitleFragment()
            val args = Bundle()
            args.putSerializable(KEY_MOSAIC_ITEM, mosaicItem)
            fragment.arguments = args
            return fragment
        }
    }
}