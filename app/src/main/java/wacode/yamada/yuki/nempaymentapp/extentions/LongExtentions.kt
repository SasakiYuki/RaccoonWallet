package wacode.yamada.yuki.nempaymentapp.extentions

/**
 * 1NEM = 1,000,000マイクロNEM
 */
fun Long.convertNEMtoMicro():Long {
    return this * 1000000
}

fun Long.convertNEMFromMicro():Long {
    return this / 1000000
}

fun Long.convertNEMFromMicroToDouble():Double {
    return this.toDouble() / 1000000
}

