package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_pin_code.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.getDrawable
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.preference.PinCodePreference
import wacode.yamada.yuki.nempaymentapp.utils.PinCodeProvider
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseFragmentActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonConfirmDialog
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonConfirmViewModel
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonPagerDialog
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonPagerViewModel
import wacode.yamada.yuki.nempaymentapp.viewmodel.NewPinCodeSettingViewModel

class NewPinCodeSettingFragment : BaseFragment() {
    private lateinit var viewModel: NewPinCodeSettingViewModel

    private val circleList = intArrayOf(R.id.inputView1,
            R.id.inputView2,
            R.id.inputView3,
            R.id.inputView4,
            R.id.inputView5,
            R.id.inputView6)

    override fun layoutRes() = R.layout.fragment_pin_code

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupConfirmMode()
        setupButtons()
        if (isDisplayEnterPin) {
            showPinCodeSettingDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        resetViews()
    }

    private fun showPinCodeSettingDialog() {
        val list = ArrayList<SimpleMessageFragment>()
        val fragment1 = SimpleMessageFragment.newInstance(getString(R.string.pin_code_setting_fragment_dialog_1))
        val fragment2 = SimpleMessageFragment.newInstance(getString(R.string.pin_code_setting_fragment_dialog_2))
        list.add(fragment1)
        list.add(fragment2)
        val viewModel = RaccoonPagerViewModel()
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { item ->

                }
        RaccoonPagerDialog.createDialog(RaccoonPagerViewModel(), getString(R.string.pin_code_setting_fragment_dialog_title), getString(R.string.com_ok), list)
                .show(activity.supportFragmentManager, RaccoonPagerDialog::class.java.toString())
    }

    private fun setupConfirmMode() {
        if (!isDisplayEnterPin) {
            displayText.text = getString(R.string.pin_code_fragment_confirm)
        }
    }

    private fun setupButtons() {
        button1.setOnClickListener {
            addPinCode(1)
        }
        button2.setOnClickListener {
            addPinCode(2)
        }
        button3.setOnClickListener {
            addPinCode(3)
        }
        button4.setOnClickListener {
            addPinCode(4)
        }
        button5.setOnClickListener {
            addPinCode(5)
        }
        button6.setOnClickListener {
            addPinCode(6)
        }
        button7.setOnClickListener {
            addPinCode(7)
        }
        button8.setOnClickListener {
            addPinCode(8)
        }
        button9.setOnClickListener {
            addPinCode(9)
        }
        button0.setOnClickListener {
            addPinCode(0)
        }
        backButton.setOnClickListener {
            if (activity is BaseFragmentActivity) {
                (activity as BaseFragmentActivity).onBackPressed()
            }
        }
    }

    private fun addPinCode(value: Int) {
        viewModel.addPinCode(value)
        changeCircleState()

        if (viewModel.isMaxLength()) {
            if (isDisplayEnterPin) {
                viewModel.sendEvent(NewPinCodeSettingViewModel.Event.CONFIRM)
            } else {
                if (viewModel.correctPinCode()) {
                    rewriteSecretKey()
                } else {
                    context.showToast(R.string.pin_code_setting_confirm_error)
                    resetViews()
                }
            }
        }
    }

    private fun rewriteSecretKey() {
        showProgress()

        val dao = NemPaymentApplication.database.walletDao()
        async(UI) {
            bg {
                val list = dao.findAll()
                Observable.fromIterable(list)
                        .subscribe { it ->
                            WalletManager.updatePinCode(it,
                                    viewModel.cachePinCode,
                                    PinCodeProvider.getPinCode(this@NewPinCodeSettingFragment.context)!!)
                        }
                PinCodePreference.saveHash(this@NewPinCodeSettingFragment.context, PinCodeHelper.createHash(viewModel.cachePinCode.toString(Charsets.UTF_8)))
                PinCodePreference.removePinCodeForFingerprint(this@NewPinCodeSettingFragment.context)
            }.await()
            hideProgress()
            showSuccessDialog()
        }
    }

    private fun resetViews() {
        viewModel.resetStringBuilder()
        for (item in circleList) {
            changeTransparentCircle(item)
        }
    }

    private fun changeTransparentCircle(id: Int) {
        view?.findViewById<View>(id)?.background = getDrawable(context, R.drawable.frame_round_transparent)
    }

    private fun changeCircleState() {
        view?.findViewById<View>(circleList[viewModel.getPinLength() - 1])?.background = getDrawable(context, R.drawable.frame_round_white)
    }

    private fun showSuccessDialog() {
        val viewModel = RaccoonConfirmViewModel()

        viewModel.closeEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    successSettingPinCode()
                }

        RaccoonConfirmDialog.createDialog(viewModel, getString(R.string.pin_code_setting_fragment_dialog_confirm_title), getString(R.string.pin_code_setting_fragment_dialog_confirm_message), getString(R.string.com_ok)).show(activity.supportFragmentManager, "")
    }

    private fun successSettingPinCode() {
        viewModel.sendEvent(NewPinCodeSettingViewModel.Event.SUCCESS, viewModel.cachePinCode)
    }

    private val isDisplayEnterPin by lazy {
        arguments.getBoolean(KEY_IS_DISPLAY_ENTER_PIN)
    }

    companion object {
        private const val KEY_IS_DISPLAY_ENTER_PIN = "key_is_display_enter_pin"

        fun newInstance(viewModel: NewPinCodeSettingViewModel, isDisplayEnterPin: Boolean): NewPinCodeSettingFragment {
            val fragment = NewPinCodeSettingFragment()
            fragment.viewModel = viewModel
            val args = Bundle().apply {
                putBoolean(KEY_IS_DISPLAY_ENTER_PIN, isDisplayEnterPin)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
