package wacode.yamada.yuki.nempaymentapp.di.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.FragmentScope

@Module
internal abstract class MyProfileInfoModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [(MyWalletInfoFragmentModule::class)])
    abstract fun bindMyWalletInfoFragmentInjectFactory(): MyProfileInfoModule
}

@Module
class MyWalletInfoFragmentModule
