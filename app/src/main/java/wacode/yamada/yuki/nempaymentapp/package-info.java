@EpoxyDataBindingLayouts({R.layout.row_header_wallet_list, R.layout.row_new_wallet_button, R.layout.row_choose_wallet})
@PackageModelViewConfig(rClass = R.class)
@PackageEpoxyConfig(
        requireAbstractModels = true,
        requireHashCode = true,
        implicitlyAddAutoModels = true
)
package wacode.yamada.yuki.nempaymentapp;

import com.airbnb.epoxy.EpoxyDataBindingLayouts;
import com.airbnb.epoxy.PackageEpoxyConfig;
import com.airbnb.epoxy.PackageModelViewConfig;