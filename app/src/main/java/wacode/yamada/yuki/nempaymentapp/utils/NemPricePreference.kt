package wacode.yamada.yuki.nempaymentapp.utils

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.rest.ApiManager
import wacode.yamada.yuki.nempaymentapp.rest.service.ZaifApiService
import wacode.yamada.yuki.nempaymentapp.view.dialog.`interface`.SimpleCallbackDoubleInterface
import wacode.yamada.yuki.nempaymentapp.view.dialog.`interface`.SimpleNoInterface


object NemPricePreference {
    fun getNemPriceFromZaif(callbackDoubleListener: SimpleCallbackDoubleInterface, callbackNoListener: SimpleNoInterface) {
        ApiManager.builderZaif()
                .create(ZaifApiService::class.java)
                .getNemPrice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callbackDoubleListener.onCall(it.last_price)
                }, {
                    callbackNoListener.onClickNo()
                    it.printStackTrace()
                })
    }
}