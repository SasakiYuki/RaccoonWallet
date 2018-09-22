package wacode.yamada.yuki.nempaymentapp.di.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.FragmentScope
import wacode.yamada.yuki.nempaymentapp.view.fragment.profile.MyProfileInfoFragment

@Module
internal abstract class MyProfileInfoModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [(MyProfileInfoFragmentModule::class)])
    abstract fun bindMyProfileInfoFragmentInjectFactory(): MyProfileInfoFragment
}

@Module
class MyProfileInfoFragmentModule
