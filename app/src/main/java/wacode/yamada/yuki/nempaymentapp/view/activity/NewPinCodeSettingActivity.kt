package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import wacode.yamada.yuki.nempaymentapp.view.fragment.NewPinCodeSettingFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.NewPinCodeSettingViewModel

class NewPinCodeSettingActivity : BaseFragmentActivity() {
    private val viewModel = NewPinCodeSettingViewModel()

    override fun initialFragment() = NewPinCodeSettingFragment.newInstance(viewModel, true)

    override fun setLayout() = SIMPLE_FRAGMENT_ONLY_LAYOUT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.eventPublisher
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    replaceFragment(it)
                }
    }

    private fun replaceFragment(item: Pair<NewPinCodeSettingViewModel.Event, ByteArray>) {
        when (item.first) {
            NewPinCodeSettingViewModel.Event.CONFIRM -> replaceFragment(NewPinCodeSettingFragment.newInstance(viewModel, false), true)
            NewPinCodeSettingViewModel.Event.SUCCESS -> nextAction(item.second)
        }
    }

    private fun nextAction(data: ByteArray) {
        if (nextScreen == NextAction.BACK_FOR_RESULT) {
            setResult(Activity.RESULT_OK, Intent().putExtra(INTENT_PIN_CODE, data))
        } else {
            setResult(Activity.RESULT_OK)
        }
        finish()
    }

    private val nextScreen by lazy {
        intent.getSerializableExtra(INTENT_NEXT_ACTION) as NextAction
    }

    companion object {
        private const val INTENT_NEXT_ACTION = "intent_next_action"
        const val INTENT_PIN_CODE = "intent_pin_code"
        const val REQUEST_CODE_PIN_CODE_SETTING = 1111

        fun getCallingIntent(context: Context, nextAction: NextAction): Intent {
            val intent = Intent(context, NewPinCodeSettingActivity::class.java)
            intent.putExtra(INTENT_NEXT_ACTION, nextAction)
            return intent
        }
    }

    enum class NextAction {
        BACK_FOR_RESULT,
        BACK
    }
}
