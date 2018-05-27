package wacode.yamada.yuki.nempaymentapp.contract

import wacode.yamada.yuki.nempaymentapp.types.OperatorType
import wacode.yamada.yuki.nempaymentapp.presenter.CalculatorPresenter

interface CalculationContract {

    interface View : BaseView<CalculatorPresenter> {
        fun updateDisplay(value: String)

        fun updateOperation(operation: OperatorType)

        fun finish()

        fun updateRightDisplay(value: String)

        fun updateNextButton()
    }
}
