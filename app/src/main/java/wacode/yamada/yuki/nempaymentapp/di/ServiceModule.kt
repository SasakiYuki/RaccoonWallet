package wacode.yamada.yuki.nempaymentapp.di

import dagger.Module
import dagger.Provides
import wacode.yamada.yuki.nempaymentapp.rest.service.AccountService
import wacode.yamada.yuki.nempaymentapp.rest.service.HarvestService
import wacode.yamada.yuki.nempaymentapp.rest.service.MosaicService
import wacode.yamada.yuki.nempaymentapp.rest.service.TransactionService
import javax.inject.Singleton


@Module
class ServiceModule {
    @Provides
    @Singleton
    fun provideMosaicService(): MosaicService = MosaicService()

    @Provides
    @Singleton
    fun provideTransactionService(): TransactionService = TransactionService()

    @Provides
    @Singleton
    fun provideHarvestService(): HarvestService = HarvestService()

    @Provides
    @Singleton
    fun provideAccountService(): AccountService = AccountService()
}

