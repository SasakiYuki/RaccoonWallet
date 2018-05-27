package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.model.MainNavigationEntity

interface MainNavigationRowEventHandler {
    fun onClick(view: View, viewModel: MainNavigationRowViewModel)
}

interface MainNavigationRowLongEventHandlers {
    fun onLongClick(view: View, viewModel: MainNavigationRowViewModel): Boolean
}

data class MainNavigationRowViewModel(
        val mainNavigation: MainNavigationEntity
) {
    fun isHeader() = mainNavigation.isHeader
    fun text() = mainNavigation.text
    fun drawable() = mainNavigation.drawable
    fun background(context: Context) = if (isHeader()) ContextCompat.getColor(context, R.color.nemGreen) else ContextCompat.getColor(context, R.color.colorDarkWhite)
}


