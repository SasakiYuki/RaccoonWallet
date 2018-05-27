package wacode.yamada.yuki.nempaymentapp.view.activity.callback

import com.journeyapps.barcodescanner.BarcodeResult


interface QrScanCallback {
    fun onQrScanResult(result: BarcodeResult?)
}