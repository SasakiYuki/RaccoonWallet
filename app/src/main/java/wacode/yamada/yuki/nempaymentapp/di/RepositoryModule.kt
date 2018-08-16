package wacode.yamada.yuki.nempaymentapp.di

import dagger.Module
import dagger.Provides
import wacode.yamada.yuki.nempaymentapp.repository.AccountRepository
import wacode.yamada.yuki.nempaymentapp.repository.MosaicRepository
import wacode.yamada.yuki.nempaymentapp.repository.TransactionRepository
import wacode.yamada.yuki.nempaymentapp.rest.service.AccountService
import wacode.yamada.yuki.nempaymentapp.rest.service.MosaicService
import wacode.yamada.yuki.nempaymentapp.rest.service.TransactionService
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMosaicRepository(mosaicService: MosaicService) = MosaicRepository(mosaicService)

    @Provides
    @Singleton
    fun provideTransactionRepository(transactionService: TransactionService) = TransactionRepository(transactionService)

    @Provides
    @Singleton
    fun provideAccountRepository(accountService: AccountService) = AccountRepository(accountService)

    @Provides
    @Singleton
    fun provideHarvestRepository(accountService: AccountService) = AccountRepository(accountService)
}

