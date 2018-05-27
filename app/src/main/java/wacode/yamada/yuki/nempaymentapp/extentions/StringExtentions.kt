package wacode.yamada.yuki.nempaymentapp.extentions

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import org.spongycastle.util.encoders.DecoderException
import org.spongycastle.util.encoders.Hex
import java.io.UnsupportedEncodingException
import java.util.regex.Pattern


fun String.copyClipBoard(context: Context) {
    val item = ClipData.Item(this)

    val mimeType = arrayOfNulls<String>(1)
    mimeType[0] = ClipDescription.MIMETYPE_TEXT_URILIST

    val cd = ClipData(ClipDescription("text_data", mimeType), item)

    val cm = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    cm.primaryClip = cd
}

fun String.transformMosaicImageUrl(): String {
    val item = this
    if (item.contains("oa:")) {
        val oaString = item.replace("oa:", "")
        return "https://s3.amazonaws.com/open-apostille-nemgallary-production/$oaString.jpg"
    } else if (item.contains("xembook")) {
        return item
    } else if (item.contains("s3.amazonaws.com/open-apostille-nemgallary-production/")) {
        return item
    }
    return ""
}

fun String.toXemMessageByteArray(): ByteArray = this.toByteArray(Charsets.UTF_8)

@Throws(UnsupportedEncodingException::class)
fun String.hexToString(): String {
    val len = this.length
    val bytes = ByteArray(len / 2)
    var i = 0
    while (i < len) {
        bytes[i / 2] = ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i + 1], 16)).toByte()
        i += 2
    }
    return String(bytes)
}

@Throws(DecoderException::class)
fun String.toHexByteArray(): ByteArray {
    val paddedHexString = if (0 == this.length % 2) this else "0" + this
    val encodedBytes = paddedHexString.toByteArray(Charsets.UTF_8)
    return Hex.decode(encodedBytes)
}

fun String.toDisplayAddress(): String {
    val matcher = Pattern.compile("[\\s\\S]{1,6}").matcher(this)

    var address = ""
    while (matcher.find()) {
        val text = matcher.group()

        if (text.length == 4) {
            address += text
        } else {
            address += text + "-"
        }
    }
    return address
}

fun String.remove(target: String): String {
    val charArray = this.toCharArray()
    val stringBuilder = StringBuilder()
    charArray
            .filter { it != target.single() }
            .forEach { stringBuilder.append(it) }
    return String(stringBuilder)
}