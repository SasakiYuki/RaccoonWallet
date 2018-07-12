package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.journeyapps.barcodescanner.BarcodeResult
import com.ryuta46.nemkotlin.account.AccountGenerator
import com.ryuta46.nemkotlin.enums.Version
import com.ryuta46.nemkotlin.util.ConvertUtils
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.spongycastle.crypto.InvalidCipherTextException
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.hexToString
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.extentions.toHexByteArray
import wacode.yamada.yuki.nempaymentapp.model.WalletQrEntity
import wacode.yamada.yuki.nempaymentapp.utils.AesCryptographer
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.ImportWalletCallback
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.QrScanCallback
import wacode.yamada.yuki.nempaymentapp.view.fragment.ImportSecretKeyFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.ImportWalletNameFragment


class WalletImportActivity : BaseFragmentActivity(), ImportWalletCallback, QrScanCallback {
    private var walletEntity: WalletQrEntity? = null

    override fun setLayout() = R.layout.activity_container
    override fun initialFragment() = ImportSecretKeyFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBarBackButton()
    }

    override fun onReplaceImportWalletName(data: ByteArray) {
        replaceFragment(ImportWalletNameFragment.newInstance(data), true)
    }

    override fun onQrScanResult(result: BarcodeResult?) {
        result?.let {
            if (it.result.text.contains("priv_key")) {
                walletEntity = Gson().fromJson(it.result.text, WalletQrEntity::class.java)
                startActivityForResult(Intent(ImportWalletPasswordActivity.getCallingIntent(this)), REQUEST_CODE_PASSWORD)
            } else {
                showToast(R.string.qr_scan_fragment_scan_error)
                onBackPressed()
            }
        } ?: run {
            showToast(R.string.qr_scan_fragment_scan_error)
            onBackPressed()
        }
    }

    override fun navigateCompleteImportWallet() {
        startActivity(CompleteImportWalletActivity.getCallingIntent(this))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PASSWORD) {
            async(UI) {
                showProgress()
                val password = data!!.getByteArrayExtra(ImportWalletPasswordActivity.KEY_PASSWORD).toString(Charsets.UTF_8)
                walletEntity?.let { it ->
                    try {
                        val rawKey = AesCryptographer.decrypt(it.data.priv_key.toHexByteArray(), it.data.salt.toHexByteArray(), password).hexToString()
                        val privateKey = if (rawKey.length == 66 && rawKey.startsWith("00")) {
                            rawKey.substring(2, 66)
                        } else {
                            rawKey
                        }

                        val account = AccountGenerator.fromSeed(ConvertUtils.swapByteArray(privateKey.toHexByteArray()), Version.Main)
                        bg { WalletManager.save(this@WalletImportActivity, account, it.data.name) }.await()
                        navigateCompleteImportWallet()
                    } catch (e: InvalidCipherTextException) {
                        e.printStackTrace()
                        showToast(R.string.import_secret_key_invalid_password)
                        onBackPressed()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        showToast(R.string.import_secret_key_import_error)
                        onBackPressed()
                    }
                }
                hideProgress()
            }
        } else {
            onBackPressed()
        }
    }

    companion object {
        private const val REQUEST_CODE_PASSWORD = 1000
        fun getCallingIntent(context: Context) = Intent(context, WalletImportActivity::class.java)
    }
}