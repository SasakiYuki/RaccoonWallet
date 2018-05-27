package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.utils.PinCodeProvider
import wacode.yamada.yuki.nempaymentapp.view.fragment.*

class PrivateKeyStoreSupportActivity : BaseFragmentActivity(), OnPrivateKeyStorePageChangeListener, SelectTutorialLevelListener {
    private var currentDisplayType = PrivateKeyStoreSupportDisplayType.TUTORIAL

    override fun setLayout() = SIMPLE_FRAGMENT_ONLY_LAYOUT
    override fun initialFragment() = TutorialDescriptionFragment.newInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBarBackButton()
    }

    override fun onClickNextButton(type: PrivateKeyStoreSupportDisplayType) {
        when (type) {
            PrivateKeyStoreSupportDisplayType.TUTORIAL ->
                replaceSelectTutorialLevelFragment()
            PrivateKeyStoreSupportDisplayType.NEWBIE_DESCRIPTION,
            PrivateKeyStoreSupportDisplayType.RACCOON_DESCRIPTION ->
                replacePrivateKeyDescriptionFragment()
            PrivateKeyStoreSupportDisplayType.LOGIN_DESCRIPTION ->
                intentPinCodeSettingActivity()
            PrivateKeyStoreSupportDisplayType.PRIVATE_KEY_DESCRIPTION ->
                replacePrivateKeyDisplayFragment()
        }
    }

    override fun onSelectTutorialType(type: PrivateKeyStoreSupportTutorialType) {
        when (type) {
            PrivateKeyStoreSupportTutorialType.NEWBIE ->
                replaceTutorialNewbieDescriptionFragment()
            PrivateKeyStoreSupportTutorialType.LOGIN ->
                replaceTutorialLoginDescriptionFragment()
            PrivateKeyStoreSupportTutorialType.RACCOON ->
                replaceTutorialRaccoonDescriptionFragment()
        }
    }

    private fun intentPinCodeSettingActivity() {
        startActivityForResult(NewPinCodeSettingActivity.getCallingIntent(
                this,
                NewPinCodeSettingActivity.NextAction.BACK_FOR_RESULT), REQUEST_CODE_PIN_CODE_SETTING)
    }

    private fun replaceSelectTutorialLevelFragment() {
        currentDisplayType = PrivateKeyStoreSupportDisplayType.SELECT_LEVEL
        val fragment = SelectTutorialLevelFragment.newInstance(this)
        replaceFragment(fragment, true)
    }

    private fun replacePrivateKeyDescriptionFragment() {
        currentDisplayType = PrivateKeyStoreSupportDisplayType.PRIVATE_KEY_DESCRIPTION
        val fragment = PrivateKeyDescriptionFragment.newInstance(this)
        replaceFragment(fragment, true)
    }

    private fun replaceTutorialNewbieDescriptionFragment() {
        currentDisplayType = PrivateKeyStoreSupportDisplayType.NEWBIE_DESCRIPTION
        val fragment = TutorialNewbieDescriptionFragment.newInstance(this)
        replaceFragment(fragment, true)
    }

    private fun replaceTutorialLoginDescriptionFragment() {
        currentDisplayType = PrivateKeyStoreSupportDisplayType.RACCOON_DESCRIPTION
        val fragment = TutorialLoginDescriptionFragment.newInstance(this)
        replaceFragment(fragment, true)
    }

    private fun replaceTutorialRaccoonDescriptionFragment() {
        currentDisplayType = PrivateKeyStoreSupportDisplayType.RACCOON_DESCRIPTION
        val fragment = TutorialRaccoonDescriptionFragment.newInstance(this)
        replaceFragment(fragment, true)
    }

    private fun replacePrivateKeyDisplayFragment() {
        currentDisplayType = PrivateKeyStoreSupportDisplayType.PRIVATE_KEY_DISPLAY
        val fragment = PrivateKeyDisplayFragment.newInstance(this)
        replaceFragment(fragment, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                when (requestCode) {
                    REQUEST_CODE_PIN_CODE_SETTING -> {
                        PinCodeProvider.setPinCode(it.getByteArrayExtra(NewPinCodeSettingActivity.INTENT_PIN_CODE).toString(Charsets.UTF_8))
                        startActivity(FingerPrintSettingActivity.createIntent(this))
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PIN_CODE_SETTING = 1119
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, PrivateKeyStoreSupportActivity::class.java)
            return intent
        }
    }
}

interface OnPrivateKeyStorePageChangeListener {
    fun onClickNextButton(type: PrivateKeyStoreSupportDisplayType)
}

interface SelectTutorialLevelListener {
    fun onSelectTutorialType(type: PrivateKeyStoreSupportTutorialType)
}

enum class PrivateKeyStoreSupportTutorialType {
    NEWBIE,
    LOGIN,
    RACCOON
}

enum class PrivateKeyStoreSupportDisplayType {
    TUTORIAL,
    SELECT_LEVEL,
    NEWBIE_DESCRIPTION,
    PRIVATE_KEY_DESCRIPTION,
    PRIVATE_KEY_DISPLAY,
    LOGIN_DESCRIPTION,
    RACCOON_DESCRIPTION
}

