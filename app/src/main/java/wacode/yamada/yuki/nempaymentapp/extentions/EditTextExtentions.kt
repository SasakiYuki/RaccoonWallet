package wacode.yamada.yuki.nempaymentapp.extentions

import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView


fun EditText.isNotTextEmptyObservable() = RxTextView.textChanges(this).map { it -> it.isNotEmpty() }