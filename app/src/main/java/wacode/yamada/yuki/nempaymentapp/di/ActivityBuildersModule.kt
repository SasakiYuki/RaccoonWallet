package wacode.yamada.yuki.nempaymentapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.activity.AddressBookActivityModule
import wacode.yamada.yuki.nempaymentapp.di.activity.MainActivityModule
import wacode.yamada.yuki.nempaymentapp.di.activity.SendActivityModule
import wacode.yamada.yuki.nempaymentapp.di.fragment.FriendInfoModule
import wacode.yamada.yuki.nempaymentapp.di.fragment.HomeBuildersModule
import wacode.yamada.yuki.nempaymentapp.di.fragment.SendBuildersModule
import wacode.yamada.yuki.nempaymentapp.view.activity.*
import wacode.yamada.yuki.nempaymentapp.view.activity.BalanceActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.MainActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.CropImageActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.SendActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.profile.ProfileAddressAddActivity


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
    @ContributesAndroidInjector(modules = [(AddressBookActivityModule::class), (FriendInfoModule::class)])
    abstract fun bindAddressBookActivity(): AddressBookActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindProfileAddressAddActivity(): ProfileAddressAddActivity
}
