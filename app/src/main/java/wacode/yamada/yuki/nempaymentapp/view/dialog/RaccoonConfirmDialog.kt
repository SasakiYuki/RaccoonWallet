package wacode.yamada.yuki.nempaymentapp.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.utils.SharedPreferenceUtils

class RaccoonConfirmDialog : SimpleDialogFragment() {
    private val title by lazy {
        arguments?.getString(ARG_TITLE, "")
    }

    private val message by lazy {
        arguments?.getString(ARG_MESSAGE, "")
    }

    private val buttonText by lazy {
        arguments?.getString(ARG_BUTTON_TEXT, "")
    }

    private lateinit var viewModel: RaccoonConfirmViewModel
    override fun setLayout() = R.layout.dialog_simple_confirm

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        setupButtons(dialog)
        setupTitle(dialog)
        setupMessage(dialog)
        return dialog
    }

    private fun setupTitle(dialog: Dialog) {
        dialog.findViewById<TextView>(R.id.titleTextView).text = title
    }

    private fun setupMessage(dialog: Dialog) {
        dialog.findViewById<TextView>(R.id.messageTextView).text = message
    }

    private fun setupButtons(dialog: Dialog) {
        val button = dialog.findViewById<Button>(R.id.button)
        button.text = buttonText
        button.setOnClickListener {
            viewModel.onClickButton()
            dismiss()
        }
        dialog.findViewById<CheckBox>(R.id.checkbox).setOnCheckedChangeListener { _, b ->
            viewModel.onCheck(b)
        }
        arguments?.let {
            val showTwiceDisplayCheckBox = it.getBoolean(ARG_SHOW_TWICE_DISPLAY_CHECK_BOX, false)
            dialog.findViewById<LinearLayout>(R.id.twiceDisplayLayout).visibility = if (showTwiceDisplayCheckBox) View.VISIBLE else View.GONE
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        viewModel.onClose()
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_BUTTON_TEXT = "button_text"
        private const val ARG_SHOW_TWICE_DISPLAY_CHECK_BOX = "show_twice_display_check_box"
        fun createDialog(viewModel: RaccoonConfirmViewModel, title: String, message: String, buttonText: String, showTwiceDisplayCheckBox: Boolean = false): RaccoonConfirmDialog {
            val dialog = RaccoonConfirmDialog()
            dialog.viewModel = viewModel
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putString(ARG_BUTTON_TEXT, buttonText)
                putBoolean(ARG_SHOW_TWICE_DISPLAY_CHECK_BOX, showTwiceDisplayCheckBox)
            }
            dialog.arguments = args
            return dialog
        }
    }
}

class RaccoonConfirmViewModel {
    val clickEvent: PublishSubject<Unit> = PublishSubject.create()
    val checkEvent: PublishSubject<Boolean> = PublishSubject.create()
    val closeEvent: PublishSubject<Unit> = PublishSubject.create()

    fun onClickButton() {
        clickEvent.onNext(Unit)
    }

    fun onCheck(checked: Boolean) {
        checkEvent.onNext(checked)
    }

    fun onClose() {
        closeEvent.onNext(Unit)
    }

    fun saveSPTwiceDisplay(context: Context, spKey: String, checked: Boolean) = SharedPreferenceUtils.put(context, spKey, checked)

    fun shouldTwiceDisplay(context: Context, spKey: String) = SharedPreferenceUtils[context, spKey, true]
}
