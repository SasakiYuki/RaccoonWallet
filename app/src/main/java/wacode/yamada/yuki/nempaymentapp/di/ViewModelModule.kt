package wacode.yamada.yuki.nempaymentapp.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import wacode.yamada.yuki.nempaymentapp.viewmodel.BalanceListViewModel
import wacode.yamada.yuki.nempaymentapp.viewmodel.CropImageViewModel
import wacode.yamada.yuki.nempaymentapp.viewmodel.EnterMosaicListViewModel
import wacode.yamada.yuki.nempaymentapp.viewmodel.HomeViewModel

@Suppress("unused")
@Module
internal abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BalanceListViewModel::class)
    abstract fun bindBalanceListViewModel(viewModel: BalanceListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EnterMosaicListViewModel::class)
    abstract fun bindEnterMosaicListViewModel(viewModel: EnterMosaicListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CropImageViewModel::class)
    abstract fun bindCropImageViewModel(viewModel: CropImageViewModel): ViewModel
}