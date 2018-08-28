package wacode.yamada.yuki.nempaymentapp.di

import dagger.Module

@Module(includes = [(ServiceModule::class),(RepositoryModule::class),(ViewModelModule::class)])
class AppModule