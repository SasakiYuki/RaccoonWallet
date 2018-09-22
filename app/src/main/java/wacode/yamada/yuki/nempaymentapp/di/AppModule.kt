package wacode.yamada.yuki.nempaymentapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import javax.inject.Singleton

@Module(includes = [(ServiceModule::class),(RepositoryModule::class),(ViewModelModule::class)])
internal class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: NemPaymentApplication): Context = application
}