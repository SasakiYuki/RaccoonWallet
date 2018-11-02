package wacode.yamada.yuki.nempaymentapp.di.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.FragmentScope
import wacode.yamada.yuki.nempaymentapp.view.fragment.FriendInfoFragment


@Module
internal abstract class FriendInfoModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [(FriendInfoFragmentModule::class)])
    abstract fun bindFriendInfoFragmentInjectFactory(): FriendInfoFragment
}

@Module
class FriendInfoFragmentModule