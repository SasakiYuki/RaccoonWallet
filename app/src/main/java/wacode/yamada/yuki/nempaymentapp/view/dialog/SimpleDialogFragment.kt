package wacode.yamada.yuki.nempaymentapp.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.LinearLayout


abstract class SimpleDialogFragment : DialogFragment() {

    abstract fun setLayout(): Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity)

        dialog.setContentView(setLayout())
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }
}