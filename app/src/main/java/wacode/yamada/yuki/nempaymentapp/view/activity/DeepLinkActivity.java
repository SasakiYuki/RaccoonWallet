package wacode.yamada.yuki.nempaymentapp.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;

import wacode.yamada.yuki.nempaymentapp.view.activity.deeplink.LibraryDeepLinkModuleLoader;
import wacode.yamada.yuki.nempaymentapp.view.activity.deeplink.SimpleModule;
import wacode.yamada.yuki.nempaymentapp.view.activity.deeplink.LibraryDeepLinkModule;
import wacode.yamada.yuki.nempaymentapp.view.activity.deeplink.SimpleModuleLoader;

@DeepLinkHandler({ SimpleModule.class, LibraryDeepLinkModule.class })
public class DeepLinkActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLinkDelegate deepLinkDelegate =
                new DeepLinkDelegate(new SimpleModuleLoader(), new LibraryDeepLinkModuleLoader());
        deepLinkDelegate.dispatchFrom(this);
        finish();
    }
}
