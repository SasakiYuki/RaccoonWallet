package wacode.yamada.yuki.nempaymentapp.presenter

import wacode.yamada.yuki.nempaymentapp.types.CalculatorNumberType


interface CalculatorPresenter {

    fun typeNumber(key: CalculatorNumberType)

    fun add()

    fun minus()

    fun divide()

    fun multiply()

    fun typeDot()

    fun typeEquals()

    fun backspace()

    fun exit()

    fun ce()
}

