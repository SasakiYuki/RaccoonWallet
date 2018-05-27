package wacode.yamada.yuki.nempaymentapp.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.R

class RaccoonSelectDialog : SimpleDialogFragment() {
    private lateinit var viewModel: RaccoonSelectViewModel
    override fun setLayout() = R.layout.dialog_simple_select

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        setupTitle(dialog)
        setupMessage(dialog)
        setupButtons(dialog)
        return dialog
    }

    private fun setupTitle(dialog: Dialog) {
        dialog.findViewById<TextView>(R.id.titleTextView).text = getTitle()
    }

    private fun setupMessage(dialog: Dialog) {
        dialog.findViewById<TextView>(R.id.messageTextView).text = getMessage()
    }

    private fun setupButtons(dialog: Dialog) {
        val leftTextView = dialog.findViewById<TextView>(R.id.negativeButton)
        leftTextView.text = viewModel.negativeButtonText
        leftTextView.setOnClickListener {
            viewModel.clickNegativeButton()
            this.dismiss()
        }
        val rightTextView = dialog.findViewById<TextView>(R.id.positiveButton)
        rightTextView.text = viewModel.positiveButtonText
        rightTextView.setOnClickListener {
            viewModel.clickPositiveButton()
            this.dismiss()
        }
    }

    private fun getTitle() = arguments.getString(ARG_TITLE,"")
    private fun getMessage() = arguments.getString(ARG_MESSAGE,"")

    companion object {
        private const val ARG_TITLE = "title";
        private const val ARG_MESSAGE = "message"
        fun createDialog(viewModel: RaccoonSelectViewModel, title: String, message: String): RaccoonSelectDialog {
            val dialog = RaccoonSelectDialog()
            dialog.viewModel = viewModel
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
            }
            dialog.arguments = args
            return dialog
        }
    }
}

class RaccoonSelectViewModel(val positiveButtonText: String, val negativeButtonText: String) {
    val clickEvent: PublishSubject<SelectDialogButton> = PublishSubject.create()

    fun clickPositiveButton() {
        onClickButton(SelectDialogButton.POSITIVE)
    }

    fun clickNegativeButton() {
        onClickButton(SelectDialogButton.NEGATIVE)
    }

    private fun onClickButton(selectDialogButton: SelectDialogButton) {
        clickEvent.onNext(selectDialogButton)
    }
}

enum class SelectDialogButton {
    POSITIVE,
    NEGATIVE
}