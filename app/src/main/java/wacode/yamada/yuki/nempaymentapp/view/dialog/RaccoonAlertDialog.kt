package wacode.yamada.yuki.nempaymentapp.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.R


class RaccoonAlertDialog : SimpleDialogFragment() {
    private lateinit var viewModel: RaccoonAlertViewModel
    override fun setLayout() = R.layout.dialog_simple_alert

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        setupButtons(dialog)
        setupTitle(dialog)
        setupMessage(dialog)
        return dialog
    }

    private fun setupTitle(dialog: Dialog) {
        dialog.findViewById<TextView>(R.id.titleTextView).text = getTitle()
    }

    private fun setupMessage(dialog: Dialog) {
        dialog.findViewById<TextView>(R.id.messageTextView).text = getMessage()
    }

    private fun setupButtons(dialog: Dialog) {
        val button = dialog.findViewById<Button>(R.id.button)
        button.text = getButtonText()
        button.setOnClickListener {
            viewModel.onClickBottomButton()
            dismiss()
        }

        dialog.findViewById<ImageView>(R.id.closeButton)
                .setOnClickListener {
                    viewModel.onClickCloseButton()
                    dismiss()
                }
    }

    private fun getTitle() = arguments?.getString(ARG_TITLE, "")
    private fun getMessage() = arguments?.getString(ARG_MESSAGE, "")
    private fun getButtonText() = arguments?.getString(ARG_BUTTON_TEXT, "")

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_BUTTON_TEXT = "button_text"
        fun createDialog(viewModel: RaccoonAlertViewModel, title: String, message: String, buttonText: String): RaccoonAlertDialog {
            val dialog = RaccoonAlertDialog()
            dialog.viewModel = viewModel
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

class RaccoonAlertViewModel {
    val clickEvent: PublishSubject<RaccoonAlertType> = PublishSubject.create()
    val closeEvent: PublishSubject<Unit> = PublishSubject.create()

    fun onClickCloseButton() {
        onClickButton(RaccoonAlertType.CLOSE_BUTTON)
    }

    fun onClickBottomButton() {
        onClickButton(RaccoonAlertType.BOTTOM_BUTTON)
    }

    fun onClose() {
        closeEvent.onNext(Unit)
    }

    private fun onClickButton(type: RaccoonAlertType) {
        clickEvent.onNext(type)
    }
}

enum class RaccoonAlertType {
    BOTTOM_BUTTON,
    CLOSE_BUTTON
}
