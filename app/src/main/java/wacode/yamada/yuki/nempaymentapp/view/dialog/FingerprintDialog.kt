package wacode.yamada.yuki.nempaymentapp.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.util.Base64
import android.view.View
import kotlinx.android.synthetic.main.dialog_fingerprint.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.getColor
import wacode.yamada.yuki.nempaymentapp.helper.FingerprintHelper
import wacode.yamada.yuki.nempaymentapp.preference.FingerprintPreference
import wacode.yamada.yuki.nempaymentapp.preference.PinCodePreference
import java.security.KeyStore
import javax.crypto.Cipher


class FingerprintDialog : SimpleDialogFragment() {
    private lateinit var iv: ByteArray
    private lateinit var listener: FingerprintCallback
    private lateinit var cancellationSignal: CancellationSignal

    override fun setLayout() = R.layout.dialog_fingerprint

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (!FingerprintHelper.checkForLaunch(context)) {
            throw RuntimeException("fingerprint is not available")
        }

        val dialog = super.onCreateDialog(savedInstanceState)
        setupViews(dialog)

        val keyStore = FingerprintHelper.initAndGetKeyStore()
        FingerprintHelper.createKeyPair(keyStore)
        startAuthenticate(keyStore)

        return dialog
    }

    private fun setupViews(dialog: Dialog) {
        dialog.setCanceledOnTouchOutside(false)
        renderViews(dialog, Stage.DEFAULT)

        dialog.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun startAuthenticate(keyStore: KeyStore) {
        if (iv.isEmpty()) {
            throw RuntimeException("iv is not initialize")
        }

        cancellationSignal = CancellationSignal()
        val cryptoObject = FingerprintManager.CryptoObject(FingerprintHelper.initAndGetCipherObject(keyStore, iv))

        context.getSystemService(FingerprintManager::class.java).authenticate(cryptoObject, cancellationSignal,
                0, object : FingerprintManager.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                    renderViews(dialog, Stage.ERROR)

                    FingerprintPreference.saveDialogState(context, false)

                    dialog.themeBackground.postDelayed({
                        dismiss()
                        listener.onError()
                    }, 2000)
                }
            }

            override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                renderViews(dialog, Stage.SUCCESS)
                dialog.themeBackground.postDelayed({
                    dismiss()
                    listener.onSuccess(result!!.cryptoObject.cipher)
                }, 1300)
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                super.onAuthenticationHelp(helpCode, helpString)
                renderViews(dialog, Stage.FAILED)
                dialog.themeBackground.postDelayed({
                    renderViews(dialog, Stage.DEFAULT)
                }, 1300)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                renderViews(dialog, Stage.FAILED)
                dialog.themeBackground.postDelayed({
                    renderViews(dialog, Stage.DEFAULT)
                }, 1300)
            }
        }, null)
    }

    private fun renderViews(dialog: Dialog, stage: Stage) {
        dialog.authenticateTitle.text = getString(stage.titleRes)
        dialog.authenticateMessage.text = getString(stage.messageRes)
        dialog.authenticateIcon.setImageDrawable(context.getDrawable(stage.iconRes))

        when (stage) {
            Stage.DEFAULT -> {
                dialog.themeBackground.setBackgroundColor(getColor(context, R.color.nemGreen))
                dialog.cancelButton.visibility = View.VISIBLE
            }
            Stage.SUCCESS -> {
                dialog.themeBackground.setBackgroundColor(getColor(context, R.color.nemGreen))
                dialog.cancelButton.visibility = View.GONE
            }
            Stage.FAILED -> {
                dialog.themeBackground.setBackgroundColor(getColor(context, R.color.colorErrorOrange))
                dialog.cancelButton.visibility = View.VISIBLE
            }
            Stage.ERROR -> {
                dialog.themeBackground.setBackgroundColor(getColor(context, R.color.colorErrorOrange))
                dialog.cancelButton.visibility = View.GONE
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        cancellationSignal.cancel()
        super.onDismiss(dialog)
    }

    enum class Stage(val titleRes: Int, val messageRes: Int, val iconRes: Int) {
        DEFAULT(R.string.fingerprint_authenticate_title, R.string.fingerprint_authenticate_message, R.drawable.icon_fingerprint_green),
        SUCCESS(R.string.fingerprint_authenticate_success_title, R.string.fingerprint_authenticate_success_message, R.drawable.icon_check_green),
        FAILED(R.string.fingerprint_authenticate_failed_title, R.string.fingerprint_authenticate_failed_message, R.drawable.icon_priority_high_red),
        ERROR(R.string.fingerprint_authenticate_error_over_limit_title, R.string.fingerprint_authenticate_error_over_limit_message, R.drawable.icon_priority_high_red)
    }

    companion object {
        fun createDialog(context: Context, listener: FingerprintDialog.FingerprintCallback): FingerprintDialog {
            val dialog = FingerprintDialog()
            dialog.iv = Base64.decode(PinCodePreference.getIv(context), Base64.DEFAULT)
            dialog.listener = listener
            return dialog
        }
    }

    interface FingerprintCallback {
        fun onSuccess(cipher: Cipher)

        fun onError()
    }
}