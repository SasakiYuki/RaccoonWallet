package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.fragment.ChooseStartSecurityFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.CreateWalletFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.CreatedWalletFragment

class CreateWalletActivity : BaseFragmentActivity(), OnCreateWalletPageChangeListener {
    private var currentDisplayType = CreateWalletDisplayType.CREATE
    private var walletName: String = ""

    override fun setLayout() = R.layout.activity_container
    override fun initialFragment() = CreateWalletFragment.newInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBarBackButton()
    }

    override fun onClickNextButton(type: CreateWalletDisplayType) {
        when (type) {
            CreateWalletDisplayType.CREATE -> replaceCreatedFragment()
            CreateWalletDisplayType.CREATED -> replaceChooseStartSecurity(R.string.choose_start_security_fragment_description_for_first)
            CreateWalletDisplayType.CREATED_ALREADY -> replaceChooseStartSecurity(R.string.choose_start_security_fragment_description_for_second)
            CreateWalletDisplayType.FINISH,
            CreateWalletDisplayType.FINISH_ALREADY -> intentPrivateKeyStoreSupportActivity()
        }
    }

    override fun onNameConfirm(name: String) {
        walletName = name
    }

    private fun replaceCreateFragment() {
        currentDisplayType = CreateWalletDisplayType.CREATE
        val fragment = CreateWalletFragment.newInstance(this)
        replaceFragment(fragment, true)
    }

    private fun replaceCreatedFragment() {
        currentDisplayType = CreateWalletDisplayType.CREATED
        val fragment = CreatedWalletFragment.newInstance(walletName, this)
        replaceFragment(fragment, true)
    }

    private fun replaceChooseStartSecurity(messageRes: Int) {
        currentDisplayType = CreateWalletDisplayType.FINISH
        val fragment = ChooseStartSecurityFragment.newInstance(this, messageRes)
        replaceFragment(fragment, true)
    }

    private fun intentPrivateKeyStoreSupportActivity() {
        startActivity(PrivateKeyStoreSupportActivity.createIntent(this))
    }

    override fun onBackPressed() {
        if (currentDisplayType == CreateWalletDisplayType.FINISH_ALREADY ||
                currentDisplayType == CreateWalletDisplayType.FINISH) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            replaceCreateFragment()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, CreateWalletActivity::class.java)
    }
}

interface OnCreateWalletPageChangeListener {
    fun onClickNextButton(type: CreateWalletDisplayType)
    fun onNameConfirm(name: String)
}

enum class CreateWalletDisplayType {
    CREATE,
    CREATED,
    CREATED_ALREADY,
    FINISH,
    FINISH_ALREADY
}
