package wacode.yamada.yuki.nempaymentapp.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import wacode.yamada.yuki.nempaymentapp.R

class AddressErrorDialog : SimpleDialogFragment() {

    override fun setLayout() = R.layout.dialog_address_error

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        setupButtons(dialog)
        setupTitle(dialog)
        setupMessage(dialog)
        return dialog
    }

    private fun setupTitle(dialog: Dialog) {
        dialog.findViewById<TextView>(R.id.titleTextView).text = getTitle
    }

    private fun setupMessage(dialog: Dialog) {
        dialog.findViewById<TextView>(R.id.messageTextView).text = getMessage
    }

    private fun setupButtons(dialog: Dialog) {
        val button = dialog.findViewById<Button>(R.id.button)
        button.text = getButtonText
        button.setOnClickListener {
            dismiss()
        }
    }

    private val getTitle by lazy {
        arguments?.getString(ARG_TITLE, "")
    }

    private val getMessage by lazy {
        arguments?.getString(ARG_MESSAGE, "")
    }

    private val getButtonText by lazy {
        arguments?.getString(ARG_BUTTON_TEXT, "")
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_BUTTON_TEXT = "button_text"

        fun createDialog(title: String, message: String, buttonText: String): AddressErrorDialog {
            val dialog = AddressErrorDialog()
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putString(ARG_BUTTON_TEXT, buttonText)
            }
            dialog.arguments = args
            return dialog
        }
    }
}
