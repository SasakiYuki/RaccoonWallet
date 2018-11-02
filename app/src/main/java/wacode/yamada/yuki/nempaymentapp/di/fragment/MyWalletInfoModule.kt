package wacode.yamada.yuki.nempaymentapp.di.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.FragmentScope
import wacode.yamada.yuki.nempaymentapp.view.fragment.profile.MyWalletInfoFragment


@Module
internal abstract class MyWalletInfoModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [(MyWalletInfoFragmentModule::class)])
    abstract fun bindMyWalletInfoFragmentInjectFactory(): MyWalletInfoFragment
}

@Module
class MyWalletInfoFragmentModule
