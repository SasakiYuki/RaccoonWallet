package wacode.yamada.yuki.nempaymentapp.view.activity

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.utils.PinCodeProvider
import wacode.yamada.yuki.nempaymentapp.utils.WalletProvider
import wacode.yamada.yuki.nempaymentapp.view.dialog.LoadingDialogFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment


abstract class BaseActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var loadingDialog: LoadingDialogFragment? = null
    private val baseCompositeDisposable = CompositeDisposable()

    abstract fun setLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayout())

        setupViews()
    }

    override fun onResume() {
        super.onResume()
        checkClearCache()
    }

    override fun onDestroy() {
        baseCompositeDisposable.clear()
        super.onDestroy()
    }

    fun Disposable.disposeWhenDestroy() = baseCompositeDisposable.add(this)

    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
    }

    protected fun setToolbarTitle(@StringRes titleRes: Int) {
        if (titleRes == BaseFragment.DEFAULT_VALUE_VISIBLE_TOOLBAR) {
            toolbar?.visibility = View.GONE
        } else {
            toolbar?.let {
                it.visibility = View.VISIBLE
                it.title = getString(titleRes)
            }
        }
    }

    protected fun setToolBarBackButton() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun showProgress() {
        val isHidden = loadingDialog?.isHidden.let { it } ?: true

        if (isHidden) {
            loadingDialog = LoadingDialogFragment()
            loadingDialog!!.show(supportFragmentManager, LoadingDialogFragment::class.java.simpleName)
        }
    }

    protected fun hideProgress() {
        val isHidden = loadingDialog?.isHidden.let { it } ?: true

        if (!isHidden) {
            loadingDialog!!.dismiss()
            loadingDialog = null
        }
    }

    private fun checkClearCache() {
        when {
            this is MainActivity || this is ChooseCreateOrScanWalletActivity || this is SelectWalletActivity -> clearCache()
        }
    }

    private fun clearCache() {
        PinCodeProvider.clearCache()
        WalletProvider.clearCache()
    }
}