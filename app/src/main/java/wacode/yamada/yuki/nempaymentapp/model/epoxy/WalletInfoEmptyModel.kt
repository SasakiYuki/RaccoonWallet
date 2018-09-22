package wacode.yamada.yuki.nempaymentapp.model.epoxy

import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import wacode.yamada.yuki.nempaymentapp.R

@EpoxyModelClass(layout = R.layout.row_wallet_info_empty)
abstract class WalletInfoEmptyModel : DataBindingEpoxyModel()
