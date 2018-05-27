package wacode.yamada.yuki.nempaymentapp.extentions

import java.math.BigDecimal

/**
 * 小数点以下の桁数を取得
 */
fun Double.getNumberOfDigits(): Int {
    val string = this.toString()
    val index = string.indexOf(".")
    if (index == -1) {
        return 0
    }
    return string.substring(index + 1).length
}

/**
 * 11.015 ->11015000
 */
fun Double.convertMicroNEM(): Long {
    return ((this * 1000000).scaleDouble(1)).toLong()
}

fun Double.scaleDouble(newScale: Int) = BigDecimal(this).setScale(newScale, BigDecimal.ROUND_HALF_UP).toDouble()

fun Double.dividedPrice(divisor: Double): BigDecimal {
    if (divisor == null || this == null || divisor == 0.0) return BigDecimal("0.0")
    val divisorBigDecimal = BigDecimal(divisor.toString()).setScale(5)
    val dividendBigDecimal = BigDecimal(this.toString()).setScale(5)

    return dividendBigDecimal.div(divisorBigDecimal)
}

fun Double.multipliedPrice(t1: Double): BigDecimal {
    if (t1 == null || this == null) return BigDecimal("0.0")
    val t1BigDecimal = BigDecimal(t1.toString()).setScale(5)
    val t2BigDecimal = BigDecimal(this.toString()).setScale(5)

    return t1BigDecimal.times(t2BigDecimal)
}
