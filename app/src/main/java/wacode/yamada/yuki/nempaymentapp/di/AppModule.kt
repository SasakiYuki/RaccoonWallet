package wacode.yamada.yuki.nempaymentapp.di

import dagger.Module

@Module(includes = [(ServiceModlue::class),(RepositoryModule::class),(ViewModelModule::class)])
class AppModule