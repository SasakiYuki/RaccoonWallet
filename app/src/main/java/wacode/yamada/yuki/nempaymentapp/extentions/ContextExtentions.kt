package wacode.yamada.yuki.nempaymentapp.extentions

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.Toast

fun Context.showToast(textString: String) = Toast.makeText(this, textString, Toast.LENGTH_SHORT).show()
fun Context.showToast(textResId: Int) = this.showToast(this.getString(textResId))

fun Context.getColor(colorResourceId: Int) = ContextCompat.getColor(this, colorResourceId)

fun Context.checkPermission(permission: String) = ContextCompat.checkSelfPermission(this, permission)


