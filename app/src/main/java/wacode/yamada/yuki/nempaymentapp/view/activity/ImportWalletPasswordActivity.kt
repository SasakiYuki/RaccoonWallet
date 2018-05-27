package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_import_wallet_password.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.isNotTextEmptyObservable
import wacode.yamada.yuki.nempaymentapp.extentions.showToast


class ImportWalletPasswordActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_import_wallet_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        passwordEditText.isNotTextEmptyObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ item ->
                    checkButton.setImageResource(if (item) R.mipmap.icon_check_green else R.mipmap.icon_check_gray)
                    checkButton.isEnabled = item
                })


        checkButton.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra(KEY_PASSWORD, passwordEditText.text.toString().toByteArray(Charsets.UTF_8)))
            finish()
        }

        closeButton.setOnClickListener { finish() }

        clipboardButton.setOnClickListener {
            pastePassword()
        }
    }

    private fun pastePassword() {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        clipboardManager.primaryClip?.let {
            passwordEditText.setText(it.getItemAt(0).text)
        } ?: run {
            showToast(R.string.import_wallet_password_paste_error)
        }
    }

    companion object {
        const val KEY_PASSWORD = "key_password"
        fun getCallingIntent(context: Context) = Intent(context, ImportWalletPasswordActivity::class.java)
    }
}