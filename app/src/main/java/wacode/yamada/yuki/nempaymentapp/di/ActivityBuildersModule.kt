package wacode.yamada.yuki.nempaymentapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.view.activity.BalanceActivity


@Suppress("unused")
@Module
internal abstract class ActivityBuildersModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindBalanceActivity():BalanceActivity
}
