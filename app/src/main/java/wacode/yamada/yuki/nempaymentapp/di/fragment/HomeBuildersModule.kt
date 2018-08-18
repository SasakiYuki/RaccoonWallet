package wacode.yamada.yuki.nempaymentapp.di.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.FragmentScope
import wacode.yamada.yuki.nempaymentapp.view.fragment.HomeFragment


@Module
internal abstract class HomeBuildersModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [(HomeFragmentModule::class)])
    abstract fun bindHomeFragmentInjectFactory(): HomeFragment
}

@Module
class HomeFragmentModule
