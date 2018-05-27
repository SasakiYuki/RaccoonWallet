package wacode.yamada.yuki.nempaymentapp.view.fragment.send

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_select_send_mode.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.item.SendMosaicItem
import wacode.yamada.yuki.nempaymentapp.view.activity.SendType
import wacode.yamada.yuki.nempaymentapp.view.activity.SendViewModel
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class SelectSendModeFragment : BaseFragment() {
    private lateinit var viewModel: SendViewModel
    override fun layoutRes() = R.layout.fragment_select_send_mode

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    private fun setupButtons() {
        standardSendCard.setOnClickListener {
            viewModel.replaceFragment(SendType.CONFIRM, getSendMosaicItems())
        }
        messageSendCard.setOnClickListener {
            viewModel.replaceFragment(SendType.SELECT_MESSAGE, getSendMosaicItems())
        }
    }

    private fun getSendMosaicItems() = arguments.getSerializable(KEY_SEND_MOSAIC_ITEMS) as ArrayList<SendMosaicItem>

    companion object {
        private const val KEY_SEND_MOSAIC_ITEMS = "key_send_mosaic_items"
        fun newInstance(viewModel: SendViewModel, list: ArrayList<SendMosaicItem>): SelectSendModeFragment {
            val fragment = SelectSendModeFragment()
            val args = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.select_send_mode_fragment_title)
                putSerializable(KEY_SEND_MOSAIC_ITEMS, list)
            }
            fragment.viewModel = viewModel
            fragment.arguments = args
            return fragment
        }
    }
}
