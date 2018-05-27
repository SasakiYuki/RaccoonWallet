package wacode.yamada.yuki.nempaymentapp.view.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.R

class LoadingDialogFragment : SimpleDialogFragment() {
    override fun setLayout() = R.layout.dialog_loading

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.isCancelable = false
        return dialog
    }
}