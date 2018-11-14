package wacode.yamada.yuki.nempaymentapp.di.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wacode.yamada.yuki.nempaymentapp.di.FragmentScope
import wacode.yamada.yuki.nempaymentapp.view.fragment.TransactionListFragment


@Module
internal abstract class TransactionListModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [(TransactionListFragmentModule::class)])
    abstract fun bindTransactionListFragmentInjectFactory(): TransactionListFragment
}

@Module
class TransactionListFragmentModule