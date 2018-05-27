package wacode.yamada.yuki.nempaymentapp.helper

import java.math.BigDecimal
import java.math.RoundingMode

object MathHelper {
    private val defaultScale: Int = 8
    private val defaultRoundingMode: RoundingMode = RoundingMode.HALF_UP
    fun add(v1: BigDecimal, v2: BigDecimal): BigDecimal =
            v1.add(v2)

    fun subtract(v1: BigDecimal, v2: BigDecimal): BigDecimal =
            v1.subtract(v2)

    fun multiply(v1: BigDecimal, v2: BigDecimal): BigDecimal =
            v1.multiply(v2).setScale(defaultScale, defaultRoundingMode)

    fun divide(v1: BigDecimal, v2: BigDecimal): BigDecimal =
            v1.divide(v2, defaultScale, defaultRoundingMode)
}