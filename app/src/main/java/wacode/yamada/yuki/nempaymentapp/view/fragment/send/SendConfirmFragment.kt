package wacode.yamada.yuki.nempaymentapp.view.fragment.send

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.view.View
import com.ryuta46.nemkotlin.account.Account
import com.ryuta46.nemkotlin.account.AccountGenerator
import com.ryuta46.nemkotlin.account.MessageEncryption
import com.ryuta46.nemkotlin.enums.Version
import com.ryuta46.nemkotlin.model.MosaicDefinitionMetaDataPair
import com.ryuta46.nemkotlin.util.ConvertUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_send_confirm.*
import kotlinx.android.synthetic.main.view_send_fingerprint_default.*
import kotlinx.android.synthetic.main.view_send_fingerprint_error.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.convertMicroNEM
import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.helper.FingerprintHelper
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.preference.FingerprintPreference
import wacode.yamada.yuki.nempaymentapp.preference.PinCodePreference
import wacode.yamada.yuki.nempaymentapp.rest.item.SendMosaicItem
import wacode.yamada.yuki.nempaymentapp.rest.item.TransactionMosaicItem
import wacode.yamada.yuki.nempaymentapp.utils.FeeCalculator
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons.DEFAULT_NEM_NAME
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons.DEFAULT_NEM_NAMESPACE
import wacode.yamada.yuki.nempaymentapp.utils.SharedPreferenceUtils
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.NewCheckPinCodeActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.SendMessageType
import wacode.yamada.yuki.nempaymentapp.view.activity.SendType
import wacode.yamada.yuki.nempaymentapp.view.activity.SendViewModel
import wacode.yamada.yuki.nempaymentapp.view.controller.SendConfirmListController
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonConfirmDialog
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonConfirmViewModel
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class SendConfirmFragment : BaseFragment() {
    private lateinit var viewModel: SendViewModel
    private lateinit var controller: SendConfirmListController
    private var cancellationSignal: CancellationSignal? = null
    private val compositeDisposable = CompositeDisposable()
    private val list = ArrayList<TransactionMosaicItem>()

    private val address by lazy {
        arguments?.getString(KEY_SEND_ADDRESS)
    }

    private val sendMosaicItems by lazy {
        arguments?.getSerializable(KEY_SEND_MOSAIC_ITEMS) as ArrayList<SendMosaicItem>
    }

    private val message by lazy {
        arguments?.getString(KEY_SEND_MESSAGE, "")
    }

    private val senderPublicKey by lazy {
        arguments?.getString(KEY_SENDER_PUBLIC_KEY, "")
    }

    override fun layoutRes() = R.layout.fragment_send_confirm

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val message = it.getString(KEY_SEND_MESSAGE, "")
            val senderPublicKey = it.getString(KEY_SENDER_PUBLIC_KEY, "")
            val address = it.getString(KEY_SEND_ADDRESS)
            parseMessage(message, senderPublicKey, address)
        }


        setupRecycler()
        showConfirmDialog()
    }

    private fun parseMessage(message: String, senderPublicKey: String, address: String) {
        context?.let {
            val feeCalculatorAccount = AccountGenerator.fromRandomSeed(Version.Main)
            val messageByteArray = when (viewModel.sendMessageType) {
                SendMessageType.NONE,
                SendMessageType.NORMAL ->
                    message.toByteArray(Charsets.UTF_8)
                SendMessageType.CRYPT ->
                    MessageEncryption.encrypt(feeCalculatorAccount, ConvertUtils.toByteArray(senderPublicKey), message.toByteArray(Charsets.UTF_8))

            }
            val fee = FeeCalculator.calculatorFeeSendMosaicItem(sendMosaicItems, messageByteArray)
            controller = SendConfirmListController(it, fee.convertNEMFromMicroToDouble(), address, message)
        }
    }

    private fun showConfirmDialog() {
        val viewModel = RaccoonConfirmViewModel()
        viewModel.checkEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { item ->
                    context?.let {
                        saveSPTwiceDisplay(it, !item)
                    }
                }

        viewModel.closeEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { setupAuthenticateViews() }

        val dialog = RaccoonConfirmDialog.createDialog(viewModel,
                getString(R.string.send_confirm_fragment_confirm_dialog_title),
                getString(R.string.send_confirm_fragment_confirm_dialog_message),
                getString(R.string.com_ok),
                true)

        context?.let {
            if (shouldTwiceDisplay(it)) {
                dialog.show(fragmentManager, RaccoonConfirmDialog::class.java.toString())
                cancellationSignal?.cancel()
            } else {
                setupAuthenticateViews()
            }
        }
    }

    private fun setupRecycler() {
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = controller.adapter

        val list = sendMosaicItems
        controller.setData(list)
    }

    private fun setupAuthenticateViews() {
        context?.let {
            if (FingerprintHelper.checkForLaunch(it)) {
                renderStages(Stage.DEFAULT)
                pinCodeRootView.visibility = View.GONE
                setupFingerprint()
            } else if (PinCodeHelper.isAvailable(it)) {
                renderStages(Stage.NONE)
                pinCodeRootView.visibility = View.VISIBLE
            }

        }
        userPinCde1.setOnClickListener { showPinCodeAuthenticateView() }
        userPinCde2.setOnClickListener { showPinCodeAuthenticateView() }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setupFingerprint() {
        val keyStore = FingerprintHelper.initAndGetKeyStore()
        FingerprintHelper.createKeyPair(keyStore)

        context?.let {
            cancellationSignal = CancellationSignal()
            val iv = Base64.decode(PinCodePreference.getIv(it), Base64.DEFAULT)
            val cryptoObject = FingerprintManager.CryptoObject(FingerprintHelper.initAndGetCipherObject(keyStore, iv))

            it.getSystemService(FingerprintManager::class.java).authenticate(cryptoObject, cancellationSignal,
                    0, object : FingerprintManager.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                        renderStages(Stage.ERROR)

                        FingerprintPreference.saveDialogState(it, false)

                        fingerprintDefaultView.postDelayed({ setupAuthenticateViews() }, 2000)
                    }
                }

                override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    val data = Base64.decode(PinCodePreference.getEncryptData(it), Base64.DEFAULT)
                    val pin = FingerprintHelper.decrypt(result!!.cryptoObject.cipher, data).toByteArray(Charsets.UTF_8)
                    successAuthenticate(pin)
                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                    super.onAuthenticationHelp(helpCode, helpString)
                    renderStages(Stage.FAILED)
                    fingerprintDefaultView.postDelayed({ renderStages(Stage.DEFAULT) }, 1300)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    renderStages(Stage.FAILED)
                    fingerprintDefaultView.postDelayed({ renderStages(Stage.DEFAULT) }, 1300)
                }
            }, null)
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
        cancellationSignal?.cancel()
    }

    private fun renderStages(stage: Stage) {
        when (stage) {
            Stage.DEFAULT -> {
                fingerprintDefaultView?.visibility = View.VISIBLE
                fingerprintErrorView.visibility = View.GONE
                fingerprintSuccessView.visibility = View.GONE
            }
            Stage.SUCCESS -> {
                fingerprintDefaultView.visibility = View.GONE
                fingerprintErrorView.visibility = View.GONE
                fingerprintSuccessView.visibility = View.VISIBLE
            }
            Stage.FAILED -> {
                fingerprintDefaultView.visibility = View.GONE
                fingerprintErrorView.visibility = View.VISIBLE
                fingerprintSuccessView.visibility = View.GONE

                errorMessage.text = getString(R.string.send_confirm_fingerprint_error)
            }
            Stage.ERROR -> {
                fingerprintDefaultView.visibility = View.GONE
                fingerprintErrorView.visibility = View.VISIBLE
                fingerprintSuccessView.visibility = View.GONE

                errorMessage.text = getString(R.string.send_confirm_fingerprint_limit_error)
            }
            Stage.NONE -> {
                fingerprintDefaultView.visibility = View.GONE
                fingerprintErrorView.visibility = View.GONE
                fingerprintSuccessView.visibility = View.GONE
            }
        }
    }

    private fun showPinCodeAuthenticateView() {
        context?.let {
            startActivityForResult(NewCheckPinCodeActivity.getCallingIntent(
                    context = it,
                    isDisplayFingerprint = false,
                    messageRes = R.string.instant_pin_code_message,
                    buttonPosition = NewCheckPinCodeActivity.ButtonPosition.RIGHT
            ), REQUEST_CODE_PIN_CODE_SUCCESS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PIN_CODE_SUCCESS -> {
                    data?.let {
                        successAuthenticate(it.getByteArrayExtra(NewCheckPinCodeActivity.INTENT_PIN_CODE))
                    }
                }
            }
        }
    }

    private fun successAuthenticate(pin: ByteArray) {
        cancellationSignal?.cancel()

        showProgress()
        renderStages(Stage.SUCCESS)

        context?.let {
            async(UI) {
                val wallet = bg { WalletManager.getSelectedWallet(it) }
                        .await()
                val account = NemCommons.createAccount(WalletManager.getPrivateKey(wallet!!, pin))
                getQuantityAndSupply(account)
            }
        }
    }

    private fun getQuantityAndSupply(account: Account) {
        if (sendMosaicItems.size > 0) {
            var count = 0
            val sendMosaicItem = sendMosaicItems[0]
            val nameSpace = sendMosaicItem.nameSpace()
            val mosaicName = sendMosaicItem.mosaicName()
            count++
            getQuantityAndSupply(account, nameSpace, mosaicName, count)
        }
    }

    private fun getQuantityAndSupply(account: Account, nameSpace: String, mosaicName: String, count: Int) {
        if (NemCommons.isNemXEM(nameSpace, mosaicName)) {
            addXEMItem()
            checkCount(account, count)
        } else {
            compositeDisposable.add(
                    NemCommons.getQuantityAndSupply(nameSpace, mosaicName)
                            .subscribe({ response ->
                                response?.let {
                                    addTransactionMosaic(response)
                                    checkCount(account, count)
                                }
                            }, { e ->
                                hideProgress()
                                e.printStackTrace()
                            }))
        }
    }

    private fun addXEMItem() {
        sendMosaicItems
                .filter { it.nameSpace() == DEFAULT_NEM_NAMESPACE && it.mosaicName() == DEFAULT_NEM_NAME }
                .mapTo(list) {
                    TransactionMosaicItem(
                            DEFAULT_NEM_NAMESPACE,
                            DEFAULT_NEM_NAME,
                            0,
                            0,
                            it.amount.convertMicroNEM())
                }
    }

    private fun checkCount(account: Account, count: Int) {
        if (count >= sendMosaicItems.size) {
            createTransaction(account)
        } else {
            val sendMosaicItem = sendMosaicItems[count]
            val nameSpace = sendMosaicItem.nameSpace()
            val mosaicName = sendMosaicItem.mosaicName()
            val newCount = count + 1
            getQuantityAndSupply(account, nameSpace, mosaicName, newCount)
        }
    }

    private fun createTransaction(account: Account) {
        arguments?.let {
            val message = it.getString(KEY_SEND_MESSAGE, "")
            val senderPublicKey = it.getString(KEY_SENDER_PUBLIC_KEY, "")
            val address = it.getString(KEY_SEND_ADDRESS)
            parseMessage(message, senderPublicKey, address)
            NemCommons.createTransaction(account, address, list, message, viewModel.sendMessageType, senderPublicKey)
                    ?.subscribe({ item ->
                        context?.let {
                            if (checkError(it, item.message)) {
                                viewModel.replaceFragment(SendType.FINISH, ArrayList())
                            }
                        }
                        hideProgress()
                    }, { e ->
                        hideProgress()
                        e.printStackTrace()
                    })?.let {
                compositeDisposable.add(it)
            }
        }
    }

    private fun addTransactionMosaic(response: MosaicDefinitionMetaDataPair) {
        for (item in sendMosaicItems) {
            if (item.nameSpace() == response.mosaic.id.namespaceId && item.mosaicName() == response.mosaic.id.name) {
                val transactionMosaicItem = TransactionMosaicItem(
                        response.mosaic.id.namespaceId,
                        response.mosaic.id.name,
                        response.mosaic.initialSupply!!,
                        response.mosaic.divisibility!!,
                        item.amount.toLong())
                list.add(transactionMosaicItem)
            }
        }
    }

    private fun checkError(context: Context, message: String): Boolean {
        return when (message) {
            CODE_SUCCESS -> {
                true
            }
            ERROR_FAILURE_INSUFFICIENT_BALANCE -> {
                context.showToast(getString(R.string.send_error_message_balance))
                false
            }
            else -> {
                context.showToast(message)
                false
            }
        }
    }

    private fun saveSPTwiceDisplay(context: Context, checked: Boolean) = SharedPreferenceUtils.put(context, SP_TWICE_DISPLAY, checked)

    private fun shouldTwiceDisplay(context: Context) = SharedPreferenceUtils[context, SP_TWICE_DISPLAY, true]

    override fun onResume() {
        super.onResume()
        setupAuthenticateViews()
    }

    companion object {
        private const val SP_TWICE_DISPLAY = "sp_twice_display_send_confirm"
        private const val KEY_SEND_MOSAIC_ITEMS = "key_send_mosaic_items"
        private const val KEY_SEND_ADDRESS = "key_send_address"
        private const val KEY_SEND_MESSAGE = "key_send_message"
        private const val KEY_SENDER_PUBLIC_KEY = "key_sender_public_key"
        private const val CODE_SUCCESS = "SUCCESS"
        private const val ERROR_FAILURE_INSUFFICIENT_BALANCE = "FAILURE_INSUFFICIENT_BALANCE"
        private const val REQUEST_CODE_PIN_CODE_SUCCESS = 1119

        fun newInstance(viewModel: SendViewModel, sendMosaicItems: ArrayList<SendMosaicItem>, address: String, message: String = "", senderPublicKey: String = ""): SendConfirmFragment {
            val fragment = SendConfirmFragment()
            fragment.viewModel = viewModel
            val arguments = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.send_confirm_fragment_title)
                putSerializable(KEY_SEND_MOSAIC_ITEMS, sendMosaicItems)
                putString(KEY_SEND_ADDRESS, address)
                putString(KEY_SEND_MESSAGE, message)
                putString(KEY_SENDER_PUBLIC_KEY, senderPublicKey)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    enum class Stage {
        DEFAULT,
        SUCCESS,
        ERROR,
        FAILED,
        NONE
    }
}