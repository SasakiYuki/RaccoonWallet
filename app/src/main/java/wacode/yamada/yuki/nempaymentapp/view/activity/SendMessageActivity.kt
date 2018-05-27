package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_send_message.*
import wacode.yamada.yuki.nempaymentapp.R

class SendMessageActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_send_message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupCloseButton()
        setupEditText()
        setupFinishButton()
    }

    private fun setupEditText() {
        RxTextView.textChanges(editText)
                .map { item -> item.length }
                .subscribe({ item ->
                    item?.let {
                        textCountTextView.text = getString(R.string.activity_send_mosaic_prefix, it)
                    }
                })
    }

    private fun setupCloseButton() {
        closeButton.setOnClickListener {
            finish()
        }
    }

    private fun setupFinishButton() {
        toolbarCheckButton.setOnClickListener {
            val intent = Intent()
                    .putExtra(INTENT_MESSAGE, editText.text.toString())
                    .putExtra(SendActivity.KEY_SEND_MESSAGE_LIST, intent.getSerializableExtra(SendActivity.KEY_SEND_MESSAGE_LIST))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val INTENT_MESSAGE = "intent_message"
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, SendMessageActivity::class.java)
            return intent
        }
    }
}
