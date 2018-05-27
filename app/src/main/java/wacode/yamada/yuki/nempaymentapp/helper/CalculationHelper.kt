package wacode.yamada.yuki.nempaymentapp.helper

import wacode.yamada.yuki.nempaymentapp.types.OperatorType
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class CalculationHelper {

    var displayHelperNumber: CalculationDisplayHelper = CalculationDisplayHelper(8)

    var cleanDisplayOnNextInteraction: Boolean = false

    private var currentTotal: BigDecimal = BigDecimal("0")

    var currentOperation: OperatorType = OperatorType.NONE
        private set

    private var lastOperation: OperatorType = OperatorType.NONE

    private var lastInput: BigDecimal = BigDecimal("0")

    init {
        ce()
    }

    private fun applyResult(value: BigDecimal) {
        val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
        df.maximumFractionDigits = 340
        displayHelperNumber.setValue(df.format(value))
        cleanDisplayOnNextInteraction = true
    }

    fun typeNumber(number: Char) {
        if (displayHelperNumber.appendNumber(number, cleanDisplayOnNextInteraction)) {
            cleanDisplayOnNextInteraction = false
        }
    }

    fun add() {
        performOperation(OperatorType.ADDITION)
    }

    fun subtract() {
        performOperation(OperatorType.SUBTRACTION)
    }

    fun divide() {
        performOperation(OperatorType.DIVISION)
    }

    fun multiply() {
        performOperation(OperatorType.MULTIPLICATION)
    }

    private fun performOperation(operation: OperatorType) {
        this.updateTempMemory()
        currentOperation = operation
        applyResult(currentTotal)
        cleanDisplayOnNextInteraction = true
    }

    private fun updateTempMemory() {
        if (!cleanDisplayOnNextInteraction ||
                currentOperation == OperatorType.NONE ||
                currentOperation == lastOperation) {
            currentTotal = calculate(currentOperation, currentTotal, displayHelperNumber.toBigDecimal())
        }
    }

    private fun calculate(operation: OperatorType,
                          value1: BigDecimal,
                          value2: BigDecimal): BigDecimal {
        return when (operation) {
            OperatorType.ADDITION -> MathHelper.add(value1, value2)
            OperatorType.SUBTRACTION -> MathHelper.subtract(value1, value2)
            OperatorType.DIVISION -> MathHelper.divide(value1, value2)
            OperatorType.MULTIPLICATION -> MathHelper.multiply(value1, value2)
            OperatorType.NONE -> value2
        }
    }

    fun backspace() {
        if (!displayHelperNumber.removeLast()) {
            currentOperation = OperatorType.NONE
            lastOperation = OperatorType.NONE
        }
    }


    fun equals() {
        if (currentOperation != OperatorType.NONE) {
            lastOperation = currentOperation
            lastInput = displayHelperNumber.toBigDecimal()
            this.updateTempMemory()
        } else {
            currentTotal = if (lastOperation != OperatorType.NONE) {
                calculate(lastOperation, displayHelperNumber.toBigDecimal(), lastInput)
            } else {
                displayHelperNumber.toBigDecimal()
            }
        }
        applyResult(currentTotal)
        currentTotal = BigDecimal(0)
        currentOperation = OperatorType.NONE
        cleanDisplayOnNextInteraction = true
    }

    fun typeDot() {
        if (displayHelperNumber.appendDecimalSeparator(cleanDisplayOnNextInteraction)) {
            cleanDisplayOnNextInteraction = false
        }
    }

    fun ce() {
        currentTotal = BigDecimal(0)
        applyResult(currentTotal)
        currentOperation = OperatorType.NONE
        cleanDisplayOnNextInteraction = false
    }
}
