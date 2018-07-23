package wacode.yamada.yuki.nempaymentapp.view.fragment.send

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_select_message_type.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.item.SendMosaicItem
import wacode.yamada.yuki.nempaymentapp.view.activity.SendType
import wacode.yamada.yuki.nempaymentapp.view.activity.SendViewModel
import wacode.yamada.yuki.nempaymentapp.view.dialog.AddressErrorDialog
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class SelectMessageTypeFragment : BaseFragment() {
    private lateinit var viewModel: SendViewModel
    override fun layoutRes() = R.layout.fragment_select_message_type

    private val sendMosaicItems by lazy {
        arguments?.getSerializable(KEY_SEND_MOSAIC_ITEMS) as ArrayList<SendMosaicItem>
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayoutClick()
    }

    private fun setupLayoutClick() {
        normalMessageLayout.setOnClickListener {
            viewModel.putMessageTypeNormal()
            viewModel.replaceFragment(SendType.MESSAGE, sendMosaicItems)
        }
        arguments?.let { arguments ->
            cryptMessageLayout.setOnClickListener {
                if (arguments.getBoolean(KEY_CAN_CRYPT_MESSAGE)) {
                    viewModel.putMessageTypeCrypt()
                    viewModel.replaceFragment(SendType.MESSAGE, sendMosaicItems)
                } else {
                    showCanNotCryptMessage()
                }
            }
        }
    }

    private fun showCanNotCryptMessage() {
        activity?.let {
            AddressErrorDialog.createDialog(
                    getString(R.string.address_error_dialog_title),
                    getString(R.string.address_error_dialog_message),
                    getString(R.string.com_ok)
            ).show(it.supportFragmentManager, "")
        }
    }

    companion object {
        private const val KEY_SEND_MOSAIC_ITEMS = "key_send_mosaic_items"
        private const val KEY_CAN_CRYPT_MESSAGE = "key_can_crypt_message"
        fun newInstance(viewModel: SendViewModel, sendMosaicItems: ArrayList<SendMosaicItem>, canCryptMessage: Boolean): SelectMessageTypeFragment {
            val fragment = SelectMessageTypeFragment()
            fragment.viewModel = viewModel
            val arguments = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.select_message_type_fragment_title)
                putSerializable(KEY_SEND_MOSAIC_ITEMS, sendMosaicItems)
                putBoolean(KEY_CAN_CRYPT_MESSAGE, canCryptMessage)
            }
            fragment.arguments = arguments
            return fragment
        }
    }
}