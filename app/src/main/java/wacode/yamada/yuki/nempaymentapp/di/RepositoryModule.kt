package wacode.yamada.yuki.nempaymentapp.di

import dagger.Module
import dagger.Provides
import wacode.yamada.yuki.nempaymentapp.repository.MosaicRepository
import wacode.yamada.yuki.nempaymentapp.rest.service.MosaicService
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMosaicRepository(mosaicService: MosaicService) = MosaicRepository(mosaicService)
}

