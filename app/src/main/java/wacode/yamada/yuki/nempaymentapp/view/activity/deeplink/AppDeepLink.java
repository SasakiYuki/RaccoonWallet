package wacode.yamada.yuki.nempaymentapp.view.activity.deeplink;

import com.airbnb.deeplinkdispatch.DeepLinkSpec;

// TODO
@DeepLinkSpec(prefix = { "example://example" })
public @interface AppDeepLink {

    String[] value();
}