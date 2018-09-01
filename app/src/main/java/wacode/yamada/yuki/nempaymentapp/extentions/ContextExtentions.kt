package wacode.yamada.yuki.nempaymentapp.extentions

import android.content.ClipboardManager
import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.Toast
import wacode.yamada.yuki.nempaymentapp.R

fun Context.showToast(textString: String) = Toast.makeText(this, textString, Toast.LENGTH_SHORT).show()
fun Context.showToast(textResId: Int) = this.showToast(this.getString(textResId))

fun Context.checkPermission(permission: String) = ContextCompat.checkSelfPermission(this, permission)

fun Context.pasteFromClipBoard(): String {
    val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    clipboardManager.primaryClip?.let {
        return it.getItemAt(0).text.toString()
    } ?: run {
        this.showToast(R.string.com_paste_error)
        return ""
    }
}

fun Context.getColorFromResource(colorResourceId: Int) = ContextCompat.getColor(this, colorResourceId)


