package wacode.yamada.yuki.nempaymentapp.presenter

import wacode.yamada.yuki.nempaymentapp.contract.CalculationContract
import wacode.yamada.yuki.nempaymentapp.helper.CalculationHelper
import wacode.yamada.yuki.nempaymentapp.types.CalculatorNumberType
import wacode.yamada.yuki.nempaymentapp.types.OperatorType

class CalculatorPresenterImpl(private val calculatorView: CalculationContract.View) :
        CalculatorPresenter {

    private val calculationHelper: CalculationHelper

    init {
        calculatorView.presenter = this
        calculationHelper = CalculationHelper()
        updateView()
    }

    private fun executeOperationAndUpdateDisplay(operationMethod: () -> Unit) {
        operationMethod()
        updateView()
    }

    private fun updateView() {
        if (!calculationHelper.cleanDisplayOnNextInteraction && calculationHelper.currentOperation != OperatorType.NONE) {
            calculatorView.updateRightDisplay(calculationHelper.displayHelperNumber.toString())
        } else {
            calculatorView.updateDisplay(calculationHelper.displayHelperNumber.toString())
        }
        calculatorView.updateOperation(calculationHelper.currentOperation)
        calculatorView.updateNextButton()
    }

    override fun add() {
        executeOperationAndUpdateDisplay { calculationHelper.add() }
    }

    override fun minus() {
        executeOperationAndUpdateDisplay { calculationHelper.subtract() }
    }

    override fun divide() {
        executeOperationAndUpdateDisplay { calculationHelper.divide() }
    }

    override fun multiply() {
        executeOperationAndUpdateDisplay { calculationHelper.multiply() }
    }

    override fun typeDot() {
        executeOperationAndUpdateDisplay { calculationHelper.typeDot() }
    }

    override fun typeEquals() {
        executeOperationAndUpdateDisplay { calculationHelper.equals() }
    }

    override fun backspace() {
        executeOperationAndUpdateDisplay { calculationHelper.backspace() }
    }

    override fun ce() {
        executeOperationAndUpdateDisplay { calculationHelper.ce() }
    }

    override fun exit() {
        calculatorView.finish()
    }

    fun typeNumberFromString(number: String) {
        when (number) {
            "0" -> typeNumber(CalculatorNumberType.ZERO)
            "1" -> typeNumber(CalculatorNumberType.ONE)
            "2" -> typeNumber(CalculatorNumberType.TWO)
            "3" -> typeNumber(CalculatorNumberType.THREE)
            "4" -> typeNumber(CalculatorNumberType.FOUR)
            "5" -> typeNumber(CalculatorNumberType.FIVE)
            "6" -> typeNumber(CalculatorNumberType.SIX)
            "7" -> typeNumber(CalculatorNumberType.SEVEN)
            "8" -> typeNumber(CalculatorNumberType.EIGHT)
            "9" -> typeNumber(CalculatorNumberType.NINE)
            "." -> typeDot()
            else -> typeNumber(CalculatorNumberType.ZERO)
        }
    }

    override fun typeNumber(key: CalculatorNumberType) {
        when (key) {
            CalculatorNumberType.ZERO -> calculationHelper.typeNumber('0')
            CalculatorNumberType.ONE -> calculationHelper.typeNumber('1')
            CalculatorNumberType.TWO -> calculationHelper.typeNumber('2')
            CalculatorNumberType.THREE -> calculationHelper.typeNumber('3')
            CalculatorNumberType.FOUR -> calculationHelper.typeNumber('4')
            CalculatorNumberType.FIVE -> calculationHelper.typeNumber('5')
            CalculatorNumberType.SIX -> calculationHelper.typeNumber('6')
            CalculatorNumberType.SEVEN -> calculationHelper.typeNumber('7')
            CalculatorNumberType.EIGHT -> calculationHelper.typeNumber('8')
            CalculatorNumberType.NINE -> calculationHelper.typeNumber('9')
        }
        updateView()
    }
}
