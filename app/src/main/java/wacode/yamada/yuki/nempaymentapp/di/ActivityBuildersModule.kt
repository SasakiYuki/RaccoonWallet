package wacode.yamada.yuki.nempaymentapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.activity.MainActivityModule
import wacode.yamada.yuki.nempaymentapp.di.activity.SendActivityModule
import wacode.yamada.yuki.nempaymentapp.di.fragment.HomeBuildersModule
import wacode.yamada.yuki.nempaymentapp.di.fragment.SendBuildersModule
import wacode.yamada.yuki.nempaymentapp.view.activity.*


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
    @ContributesAndroidInjector(modules = [(MainActivityModule::class), (HomeBuildersModule::class)])
    abstract fun bindMain0Activity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindCropImageActivity(): CropImageActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindCreateAddressBookActivity(): CreateAddressBookActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindAddressBookActivity(): AddressBookActivity
}
