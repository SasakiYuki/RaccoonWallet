package wacode.yamada.yuki.nempaymentapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.activity.SendActivityModule
import wacode.yamada.yuki.nempaymentapp.di.fragment.SendBuildersModule
import wacode.yamada.yuki.nempaymentapp.view.activity.BalanceActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.CreateAddressBookActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.CropImageActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.SendActivity


@Suppress("unused")
@Module
internal abstract class ActivityBuildersModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindBalanceActivity(): BalanceActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(SendActivityModule::class), (SendBuildersModule::class)])
    abstract fun bindSendActivity(): SendActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindCropImageActivity(): CropImageActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindCreateAddressBookActivity(): CreateAddressBookActivity
}
