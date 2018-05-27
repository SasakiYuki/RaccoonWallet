package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import wacode.yamada.yuki.nempaymentapp.view.fragment.CreateWalletTutorialFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.SelectCreateOrScanWalletFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.ChooseCreateOrScanWalletViewModel

class ChooseCreateOrScanWalletActivity : BaseFragmentActivity() {
    private val viewModel = ChooseCreateOrScanWalletViewModel()
    override fun initialFragment() = if (shouldShowTutorial())CreateWalletTutorialFragment.newInstance(viewModel) else SelectCreateOrScanWalletFragment.newInstance()
    override fun setLayout() = BaseFragmentActivity.SIMPLE_FRAGMENT_ONLY_LAYOUT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolBarBackButton()

        viewModel.replaceEvent.
                observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    replaceFragment(SelectCreateOrScanWalletFragment.newInstance(), true)
                })
    }

    private fun shouldShowTutorial() = intent.getBooleanExtra(KEY_SHOULD_SHOW_TUTORIAL,true)

    companion object {
        private const val KEY_SHOULD_SHOW_TUTORIAL = "KEY_SHOULD_SHOW_TUTORIAL"
        fun createIntent(context: Context,shouldShowTutorial:Boolean): Intent {
            val intent = Intent(context, ChooseCreateOrScanWalletActivity::class.java)
            intent.putExtra(KEY_SHOULD_SHOW_TUTORIAL,shouldShowTutorial)
            return intent
        }
    }
}
