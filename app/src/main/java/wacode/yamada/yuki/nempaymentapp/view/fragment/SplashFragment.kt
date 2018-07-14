package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.helper.ActiveNodeHelper
import wacode.yamada.yuki.nempaymentapp.preference.AppLockPreference
import wacode.yamada.yuki.nempaymentapp.types.NodeType
import wacode.yamada.yuki.nempaymentapp.view.activity.NewCheckPinCodeActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.SplashCallback


class SplashFragment : BaseFragment() {

    private val compositeDisposable = CompositeDisposable()

    override fun layoutRes() = R.layout.fragment_splash

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ActiveNodeHelper.auto(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (AppLockPreference.isAvailable(context)) {
                        startActivityForResult(NewCheckPinCodeActivity.getCallingIntent(
                                context = context,
                                isDisplayFingerprint = true,
                                buttonPosition = NewCheckPinCodeActivity.ButtonPosition.NON),
                                REQUEST_CODE_SPLASH_AUTHENTICATE)
                    } else {
                        finishSplash()
                    }
                }, { e ->
                    ActiveNodeHelper.saveNodeType(context, NodeType.ALICE2)
                    Toast.makeText(context, R.string.splash_node_select_error, Toast.LENGTH_LONG).show()
                    finishSplash()
                }).let { addDispose(it) }
    }

    override fun onDetach() {
        super.onDetach()
        unSubscribe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SPLASH_AUTHENTICATE -> {
                    finishSplash()
                }
            }
        }
    }

    private fun addDispose(disposable: Disposable) {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.add(disposable)
        }
    }

    private fun unSubscribe() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun finishSplash() {
        activity.supportFragmentManager.beginTransaction().remove(this).commit()
        (activity as SplashCallback).hideSplash()
    }

    companion object {
        private const val REQUEST_CODE_SPLASH_AUTHENTICATE = 1115
        fun newInstance(): SplashFragment {
            val fragment = SplashFragment()
            val args = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.splash_title)
            }
            fragment.arguments = args
            return fragment
        }
    }
}