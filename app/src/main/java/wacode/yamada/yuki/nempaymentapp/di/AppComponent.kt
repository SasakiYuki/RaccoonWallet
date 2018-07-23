package wacode.yamada.yuki.nempaymentapp.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (AppModule::class), (ActivityBuildersModule::class)])
internal interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: NemPaymentApplication): Builder

        fun build(): AppComponent
    }

    fun inject(instance: NemPaymentApplication?)
}
