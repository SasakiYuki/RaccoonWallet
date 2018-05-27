package wacode.yamada.yuki.nempaymentapp.extentions

import org.spongycastle.util.encoders.Hex

fun ByteArray.hexToString(): String {
    val decodedBytes = Hex.encode(this)
    return String(decodedBytes, Charsets.UTF_8)
}