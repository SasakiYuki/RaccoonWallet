package wacode.yamada.yuki.nempaymentapp.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import wacode.yamada.yuki.nempaymentapp.viewmodel.*


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

    @Binds
    @IntoMap
    @ViewModelKey(CreateAddressBookViewModel::class)
    abstract fun bindCreateAddressBookViewModel(viewModel: CreateAddressBookViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddressBookViewModel::class)
    abstract fun bindAddressBookViewModel(viewModel: AddressBookViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FriendInfoViewModel::class)
    abstract fun bindFriendInfoViewModel(viewModel: FriendInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileAddressAddViewModel::class)
    abstract fun bindProfileAddressAddViewModel(viewModel: ProfileAddressAddViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyAddressProfileViewModel::class)
    abstract fun bindMyAddressProfileViewModel(viewModel: MyAddressProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyWalletInfoViewModel::class)
    abstract fun bindMyWalletInfoViewModel(viewModel: MyWalletInfoViewModel): ViewModel
}