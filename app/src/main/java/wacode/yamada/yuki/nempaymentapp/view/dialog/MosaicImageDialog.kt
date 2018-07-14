package wacode.yamada.yuki.nempaymentapp.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.setImage
import wacode.yamada.yuki.nempaymentapp.extentions.transformMosaicImageUrl
import wacode.yamada.yuki.nempaymentapp.rest.model.MosaicIdEntity

class MosaicImageDialog : SimpleDialogFragment() {
    override fun setLayout() = R.layout.dialog_mosaic_image

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val entity = arguments?.getSerializable(KEY_MOSAIC_ID_ENTITY) as MosaicIdEntity
        setupImageView(dialog, entity)
        setupTextView(dialog, entity)
        return dialog
    }

    private fun setupTextView(dialog: Dialog, entity: MosaicIdEntity) {
        dialog.findViewById<TextView>(R.id.textViewName).text = entity.name
        dialog.findViewById<TextView>(R.id.textViewNameSpace).text = entity.namespaceId
    }

    private fun setupImageView(dialog: Dialog, entity: MosaicIdEntity) {
        entity.imageUrl?.let {
            setImage(dialog.findViewById(R.id.imageView), it.transformMosaicImageUrl())
        }
    }

    companion object {
        private const val KEY_MOSAIC_ID_ENTITY = "key_mosaic_id_entity"
        fun createDialog(entity: MosaicIdEntity): MosaicImageDialog {
            val dialog = MosaicImageDialog()
            val args = Bundle()
            args.putSerializable(KEY_MOSAIC_ID_ENTITY, entity)
            dialog.arguments = args
            return dialog
        }
    }
}