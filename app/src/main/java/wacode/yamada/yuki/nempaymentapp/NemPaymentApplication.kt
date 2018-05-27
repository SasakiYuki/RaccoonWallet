package wacode.yamada.yuki.nempaymentapp

import android.app.Application
import android.arch.persistence.room.Room
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.Fabric
import wacode.yamada.yuki.nempaymentapp.extentions.objectOf
import wacode.yamada.yuki.nempaymentapp.room.DataBase


class NemPaymentApplication : Application() {
    companion object {
        lateinit var database: DataBase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, objectOf<DataBase>(), "room_nem_payment_app.db").build()
        FirebaseAnalytics.getInstance(this)

        Fabric.with(this, Crashlytics.Builder().core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build())
        registerActivityLifecycleCallbacks(AppLockLifecycleHandler())
    }
}