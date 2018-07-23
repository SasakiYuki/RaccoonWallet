package wacode.yamada.yuki.nempaymentapp.di

import dagger.Module
import dagger.Provides
import wacode.yamada.yuki.nempaymentapp.rest.service.MosaicService
import javax.inject.Singleton

@Module
class ServiceModlue {
    @Provides
    @Singleton
    fun proviceMosaicService(): MosaicService = MosaicService()
}

