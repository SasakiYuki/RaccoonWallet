package wacode.yamada.yuki.nempaymentapp.helper

import java.math.BigDecimal

class CalculationDisplayHelper() {

    companion object {
        val DECIMAL_SEPARATOR: Char = '.'
    }

    private var internalDisplayText: StringBuilder = StringBuilder("0")

    private var usingDecimalSymbol: Boolean = false

    private var maxLength: Int = -1

    private var isValidNumber: Boolean = true

    constructor(maxLength: Int) : this(maxLength, "0")

    constructor(maxLength: Int, initialValue: String) : this() {
        if (maxLength < -1 || maxLength == 0) {
            throw IllegalArgumentException("The argument must be any number greater than zero or -1")
        }
        this.maxLength = maxLength

        setValue(initialValue)
    }

    fun appendNumber(charNumber: Char, replaceCurrentDisplay: Boolean): Boolean {
        if (!Character.isDigit(charNumber)) {
            throw IllegalArgumentException("Invalid value, must be a number")
        }

        val isZero: Boolean = internalDisplayText.length == 1
                && internalDisplayText.toString() == "0"


        if (charNumber == '0' && isZero) {
            return false
        } else if (!isValidNumber || replaceCurrentDisplay || isZero) {
            isValidNumber = true
            usingDecimalSymbol = false
            internalDisplayText.setLength(0)
        }
        if (canAppendChar()) {
            internalDisplayText.append(charNumber)
            return true
        }
        return false
    }

    private fun canAppendChar(): Boolean {
        val realLength = internalDisplayText.length - if (usingDecimalSymbol) 1 else 0
        return maxLength == -1 || (maxLength > 0 && realLength < maxLength)
    }

    override fun toString(): String {
        return internalDisplayText.toString()
    }

    fun toBigDecimal(): BigDecimal {
        return BigDecimal(toString())
    }

    fun appendDecimalSeparator(replaceCurrentDisplay: Boolean): Boolean {
        if (replaceCurrentDisplay) {
            setValue("0")
        }
        if (!usingDecimalSymbol && isValidNumber) {
            internalDisplayText.append(DECIMAL_SEPARATOR)
            usingDecimalSymbol = true
            return true
        }
        return false
    }

    fun setValue(value: String) {
        internalDisplayText.setLength(0)

        val indexOfDecimalSeparator = value.indexOf(DECIMAL_SEPARATOR)
        val maxLengthConsideringDecimalSeparator =
                this.maxLength + if (indexOfDecimalSeparator > -1) 1 else 0

        isValidNumber = isNumber(value)
        if (isValidNumber
                && this.maxLength > -1
                && value.length > maxLengthConsideringDecimalSeparator) {
            if (indexOfDecimalSeparator == -1
                    || indexOfDecimalSeparator > this.maxLength) {
                throw ArithmeticException("The number has no precision enough")
            }
            internalDisplayText.append(value.substring(0, maxLengthConsideringDecimalSeparator))
        } else {
            internalDisplayText.append(value)
        }

        if (isValidNumber) {
            usingDecimalSymbol = internalDisplayText.contains(DECIMAL_SEPARATOR)
        }
    }

    private fun isNumber(str: String): Boolean {
        try {
            BigDecimal(str)
        } catch (e: NumberFormatException) {
            return false
        }
        return true
    }

    fun removeLast(): Boolean {
        if (internalDisplayText.length == 1 && internalDisplayText.toString()[0] == '0') {
            return false
        }
        if (!isValidNumber) {
            internalDisplayText.setLength(0)
            isValidNumber = true
        }
        if (internalDisplayText.isNotEmpty()) {
            if (internalDisplayText.toString()[internalDisplayText.length - 1] == DECIMAL_SEPARATOR) {
                usingDecimalSymbol = false
            }
            internalDisplayText.setLength(internalDisplayText.length - 1)
            if (internalDisplayText.length == 1 &&
                    !Character.isDigit(internalDisplayText.toString()[0])) {
                internalDisplayText.setLength(0)
            }
        }
        if (internalDisplayText.isEmpty()) {
            internalDisplayText.append("0")
        }
        return true
    }
}
