package wacode.yamada.yuki.nempaymentapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.preference.AppLockPreference
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.NewCheckPinCodeActivity
import wacode.yamada.yuki.nempaymentapp.view.fragment.SplashFragment


class AppLockLifecycleHandler : Application.ActivityLifecycleCallbacks {
    private var appStatus = AppStatus.FOREGROUND
    private var running = 0

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
        if (appStatus == AppStatus.RETURNED_TO_FOREGROUND) {
            activity?.let {
                val fragment = (it as BaseActivity).supportFragmentManager.findFragmentByTag(SplashFragment::class.java.simpleName)
                val isAvailableAppLock = AppLockPreference.isAvailable(it)
                if (fragment != null || activity is NewCheckPinCodeActivity || !isAvailableAppLock) return

                it.startActivity(NewCheckPinCodeActivity.getCallingIntent(
                        context = it,
                        isDisplayFingerprint = true,
                        buttonPosition = NewCheckPinCodeActivity.ButtonPosition.NON,
                        backToAppClose = true))
            }
        }
    }

    override fun onActivityStarted(activity: Activity?) {
        if (++running == 1) {
            appStatus = AppStatus.RETURNED_TO_FOREGROUND
        } else if (running > 1) {
            appStatus = AppStatus.FOREGROUND
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
        if (--running == 0) {
            appStatus = AppStatus.BACKGROUND
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }
}

enum class AppStatus {
    BACKGROUND,
    RETURNED_TO_FOREGROUND,
    FOREGROUND
}