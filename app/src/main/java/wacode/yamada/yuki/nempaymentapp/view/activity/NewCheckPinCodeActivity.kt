package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import kotlinx.android.synthetic.main.activity_new_check_pin_code.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.helper.FingerprintHelper
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.preference.FingerprintPreference
import wacode.yamada.yuki.nempaymentapp.preference.PinCodePreference
import wacode.yamada.yuki.nempaymentapp.view.dialog.FingerprintDialog
import wacode.yamada.yuki.nempaymentapp.viewmodel.NewCheckPinCodeViewModel
import javax.crypto.Cipher

class NewCheckPinCodeActivity : BaseActivity(), FingerprintDialog.FingerprintCallback {
    private val viewModel = NewCheckPinCodeViewModel()

    private val circleList = intArrayOf(R.id.inputView1,
            R.id.inputView2,
            R.id.inputView3,
            R.id.inputView4,
            R.id.inputView5,
            R.id.inputView6)

    override fun setLayout() = R.layout.activity_new_check_pin_code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!PinCodeHelper.isAvailable(this)) {
            throw RuntimeException("PinCode is not available")
        }

        setupViews()
        if (isDisplayFingerprint) {
            showFingerprintDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_slide_out_up)
        resetViews()
    }

    override fun onPause() {
        super.onPause()
        viewModel.resetStringBuilder()
        overridePendingTransition(R.anim.anim_slide_in_down, R.anim.anim_slide_out_down)
    }

    override fun onBackPressed() {
        if (backToClose) {
            moveTaskToBack(true)
        } else {
            loginError()
        }
    }

    private fun setupViews() {
        setupButtons()
        displayText.text = message

        when (buttonPosition) {
            ButtonPosition.NON -> {
                backButton.visibility = View.GONE
                closeButton.visibility = View.GONE
            }
            ButtonPosition.LEFT -> {
                backButton.visibility = View.VISIBLE
                closeButton.visibility = View.GONE
            }
            ButtonPosition.RIGHT -> {
                backButton.visibility = View.GONE
                closeButton.visibility = View.VISIBLE
            }
        }
    }

    private fun setupButtons() {
        button1.setOnClickListener {
            addPinCode(1)
        }
        button2.setOnClickListener {
            addPinCode(2)
        }
        button3.setOnClickListener {
            addPinCode(3)
        }
        button4.setOnClickListener {
            addPinCode(4)
        }
        button5.setOnClickListener {
            addPinCode(5)
        }
        button6.setOnClickListener {
            addPinCode(6)
        }
        button7.setOnClickListener {
            addPinCode(7)
        }
        button8.setOnClickListener {
            addPinCode(8)
        }
        button9.setOnClickListener {
            addPinCode(9)
        }
        button0.setOnClickListener {
            addPinCode(0)
        }
        backButton.setOnClickListener {
            loginError()
        }
        closeButton.setOnClickListener {
            loginError()
        }
    }

    private fun showFingerprintDialog() {
        if (FingerprintHelper.checkForLaunch(this)) {
            FingerprintDialog
                    .createDialog(this, this)
                    .show(supportFragmentManager, FingerprintDialog::class.java.toString())
        }
    }

    private fun addPinCode(value: Int) {
        viewModel.addPinCode(value)
        changeCircleState()
        if (viewModel.isMaxLength()) {
            if (viewModel.checkPinCode(this)) {
                loginSuccess()
            } else {
                passwordFailed()
            }
        }
    }

    private fun changeTransparentCircle(id: Int) {
        findViewById<View>(id)?.background = getDrawable(R.drawable.frame_round_transparent)
    }

    private fun changeCircleState() {
        findViewById<View>(circleList[viewModel.getPinLength() - 1])?.background = getDrawable(R.drawable.frame_round_white)
    }

    private fun resetViews() {
        viewModel.resetStringBuilder()
        for (item in circleList) {
            changeTransparentCircle(item)
        }
    }

    private fun passwordFailed() {
        showToast(R.string.pin_code_setting_confirm_error)
        resetViews()
    }

    private fun loginSuccess() {
        FingerprintPreference.saveDialogState(this, true)

        setResult(Activity.RESULT_OK, Intent().putExtra(INTENT_PIN_CODE, String(viewModel.stringBuilder).toByteArray(Charsets.UTF_8)))
        finish()
    }

    private fun loginError() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onSuccess(cipher: Cipher) {
        val data = Base64.decode(PinCodePreference.getEncryptData(this), Base64.DEFAULT)
        val pin = FingerprintHelper.decrypt(cipher, data)

        for (item in pin.toCharArray()) {
            viewModel.addPinCode(item.toString().toInt())
            changeCircleState()
        }

        if (viewModel.checkPinCode(this)) {
            loginSuccess()
        } else {
            passwordFailed()
        }
    }

    override fun onError() {
        loginError()
    }

    private val isDisplayFingerprint by lazy {
        intent.getBooleanExtra(INTENT_IS_DISPLAY_FINGERPRINT, false)
    }

    private val message by lazy {
        getString(intent.getIntExtra(INTENT_MESSAGE_RES, 0))
    }

    private val buttonPosition by lazy {
        intent.getSerializableExtra(INTENT_BUTTON_POSITION) as ButtonPosition
    }

    private val backToClose by lazy {
        intent.getBooleanExtra(INTENT_BACK_TO_APP_CLOSE, false)
    }

    companion object {
        private const val INTENT_IS_DISPLAY_FINGERPRINT = "intent_is_display_fingerprint"
        private const val INTENT_MESSAGE_RES = "intent_message_res"
        private const val INTENT_BUTTON_POSITION = "intent_button_position"
        private const val INTENT_BACK_TO_APP_CLOSE = "intent_back_to_app_close"
        const val INTENT_PIN_CODE = "intent_pin_code"
        const val REQUEST_CODE_PIN_CODE_CHECK = 1112

        fun getCallingIntent(context: Context, isDisplayFingerprint: Boolean, messageRes: Int = R.string.pin_code_fragment_enter,
                             buttonPosition: ButtonPosition, backToAppClose: Boolean = false): Intent {
            val intent = Intent(context, NewCheckPinCodeActivity::class.java)
            intent.putExtra(INTENT_IS_DISPLAY_FINGERPRINT, isDisplayFingerprint)
            intent.putExtra(INTENT_MESSAGE_RES, messageRes)
            intent.putExtra(INTENT_BUTTON_POSITION, buttonPosition)
            intent.putExtra(INTENT_BACK_TO_APP_CLOSE, backToAppClose)
            return intent
        }
    }

    enum class ButtonPosition {
        RIGHT,
        LEFT,
        NON
    }
}