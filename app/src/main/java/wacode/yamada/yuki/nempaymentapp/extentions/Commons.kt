package wacode.yamada.yuki.nempaymentapp.extentions

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ImageView
import com.google.gson.Gson
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder
import com.google.zxing.qrcode.encoder.QRCode
import com.squareup.picasso.Picasso
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.model.PaymentQREntity
import java.math.BigDecimal
import java.util.*


internal inline fun <reified T : Any> objectOf() = T::class.java

internal fun getId(): Long = Math.random().toLong()

internal fun generateQRCode(text: String): Bitmap {
    val qrCode: QRCode = Encoder.encode(text, ErrorCorrectionLevel.H)
    val byteMatrix = qrCode.matrix

    var bitmap = Bitmap.createBitmap(byteMatrix.width, byteMatrix.height, Bitmap.Config.ARGB_8888)

    for (y in 0 until byteMatrix.height) {
        for (x in 0 until byteMatrix.width) {
            val `val` = byteMatrix.get(x, y)
            bitmap.setPixel(x, y, if (`val`.toInt() == 1) Color.BLACK else Color.WHITE)
        }
    }

    bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false)

    return bitmap
}

internal fun generateQRCode(paymentQREntity: PaymentQREntity): Bitmap {
    return generateQRCode(Gson().toJson(paymentQREntity))
}

internal fun getDrawable(context: Context, resourceId: Int): Drawable? {
    return ContextCompat.getDrawable(context, resourceId)
}

internal fun getNemStartDateTimeLong(): Long {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.set(2015, 3 - 1, 29, 0, 6, 25)
    return (cal.timeInMillis)
}

internal fun scaleDouble(double: Double, newScale: Int) = BigDecimal(double).setScale(newScale, BigDecimal.ROUND_HALF_UP).toDouble()

internal fun getColor(context: Context, resourceId: Int) = ContextCompat.getColor(context, resourceId)

internal fun log(text: String) = Log.d("Log.d:", text)

@BindingAdapter("loadImg")
fun setImage(imageView: ImageView, url: String) {
    if (url.isEmpty()) {
        return
    }
    Picasso
            .with(imageView.context)
            .load(url)
            .placeholder(R.drawable.ic_refresh_black_24dp)
            .error(R.drawable.ic_broken_image_black_24dp)
            .into(imageView);
}