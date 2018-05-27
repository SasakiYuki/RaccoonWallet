package wacode.yamada.yuki.nempaymentapp.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.R

class FingerprintSettingDialog : SimpleDialogFragment() {
    private lateinit var viewModel: FingerprintSettingViewModel

    override fun setLayout() = R.layout.dialog_fingerprint_setting

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        setupViews(dialog)
        return dialog
    }

    private fun setupViews(dialog: Dialog) {
        dialog.findViewById<ImageView>(R.id.authenticateIcon).setImageResource(R.drawable.icon_fingerprint_green)

        dialog.findViewById<TextView>(R.id.okButton).setOnClickListener {
            dialog.findViewById<LinearLayout>(R.id.authenticateRootView).visibility = View.GONE
            dialog.findViewById<LinearLayout>(R.id.successRootView).visibility = View.VISIBLE
            dialog.findViewById<ImageView>(R.id.authenticateIcon).setImageResource(R.drawable.icon_check_green)
            viewModel.clickPositiveButton()
        }

        dialog.findViewById<TextView>(R.id.cancelButton).setOnClickListener {
            dismiss()
        }

        dialog.findViewById<Button>(R.id.successButton).setOnClickListener {
            viewModel.clickCompleteButton()
            dismiss()
        }
    }

    companion object {
        fun createDialog(viewModel: FingerprintSettingViewModel): FingerprintSettingDialog {
            val dialog = FingerprintSettingDialog()
            dialog.viewModel = viewModel
            return dialog
        }
    }
}

class FingerprintSettingViewModel {
    val clickEvent: PublishSubject<FingerprintSettingSelectButton> = PublishSubject.create()

    fun clickPositiveButton() {
        onClickButton(FingerprintSettingSelectButton.POSITIVE)
    }

    fun clickCompleteButton() {
        onClickButton(FingerprintSettingSelectButton.COMPLETE)
    }

    private fun onClickButton(fingerprintSettingSelectButton: FingerprintSettingSelectButton) {
        clickEvent.onNext(fingerprintSettingSelectButton)
    }
}

enum class FingerprintSettingSelectButton {
    POSITIVE,
    COMPLETE
}
