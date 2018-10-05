package wacode.yamada.yuki.nempaymentapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import wacode.yamada.yuki.nempaymentapp.repository.*
import wacode.yamada.yuki.nempaymentapp.rest.service.AccountService
import wacode.yamada.yuki.nempaymentapp.rest.service.HarvestService
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
    fun provideAddressBookRepository() = AddressBookRepository()

    fun provideTransactionRepository(transactionService: TransactionService) = TransactionRepository(transactionService)

    @Provides
    @Singleton
    fun provideAccountRepository(accountService: AccountService) = AccountRepository(accountService)

    @Provides
    @Singleton
    fun provideHarvestRepository(harvestService: HarvestService) = HarvestRepository(harvestService)

    @Provides
    @Singleton
    fun provideMyProfileRepository(context: Context) = MyProfileRepository(context)

    @Provides
    @Singleton
    fun provideMyAddressProfileRepository() = MyAddressProfileRepository()

    @Provides
    @Singleton
    fun provideMyAddressRepository() = MyAddressRepository()

    @Provides
    @Singleton
    fun provideWalletInfoRepository() = WalletInfoRepository()

    @Provides
    @Singleton
    fun provideWalletRepository() = WalletRepository()
}

