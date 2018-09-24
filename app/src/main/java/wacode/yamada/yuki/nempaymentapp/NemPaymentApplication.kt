package wacode.yamada.yuki.nempaymentapp

import android.app.Activity
import android.app.Application
import android.arch.persistence.room.Room
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import wacode.yamada.yuki.nempaymentapp.di.DaggerAppComponent
import wacode.yamada.yuki.nempaymentapp.extentions.objectOf
import wacode.yamada.yuki.nempaymentapp.room.DataBase
import wacode.yamada.yuki.nempaymentapp.room.migrations.*
import javax.inject.Inject


class NemPaymentApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)

        AndroidThreeTen.init(this)

        database = Room.databaseBuilder(this, objectOf<DataBase>(), "room_nem_payment_app.db")
                .addMigrations(migration1To2, migration2To3, migration3To4, migration4To5, migration5To6)
                .build()

        FirebaseAnalytics.getInstance(this)

        Fabric.with(this, Crashlytics.Builder().core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build())
        registerActivityLifecycleCallbacks(AppLockLifecycleHandler())
    }

    companion object {
        lateinit var database: DataBase
    }
}