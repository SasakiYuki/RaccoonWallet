package wacode.yamada.yuki.nempaymentapp.di.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.FragmentScope
import wacode.yamada.yuki.nempaymentapp.view.fragment.send.EnterSendFragment

@Module
internal abstract class SendBuildersModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [(EnterSendFragmentModule::class)])
    abstract fun bindEnterSendFragmentInjectFactory(): EnterSendFragment
}

@Module
class EnterSendFragmentModule
