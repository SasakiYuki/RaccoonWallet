package wacode.yamada.yuki.nempaymentapp.di.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.FragmentScope
import wacode.yamada.yuki.nempaymentapp.view.fragment.FriendWalletFragment


@Module
internal abstract class FriendWalletModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [(FriendWalletFragmentModule::class)])
    abstract fun bindFriendWalletFragmentInjectFactory(): FriendWalletFragment
}

@Module
class FriendWalletFragmentModule