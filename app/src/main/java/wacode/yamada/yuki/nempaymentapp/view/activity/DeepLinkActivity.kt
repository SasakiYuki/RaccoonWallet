package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.os.Bundle
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import wacode.yamada.yuki.nempaymentapp.view.activity.deeplink.LibraryDeepLinkModule
import wacode.yamada.yuki.nempaymentapp.view.activity.deeplink.LibraryDeepLinkModuleLoader
import wacode.yamada.yuki.nempaymentapp.view.activity.deeplink.SimpleModule
import wacode.yamada.yuki.nempaymentapp.view.activity.deeplink.SimpleModuleLoader

@DeepLinkHandler(SimpleModule::class, LibraryDeepLinkModule::class)
class DeepLinkActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deepLinkDelegate = DeepLinkDelegate(SimpleModuleLoader(), LibraryDeepLinkModuleLoader())
        deepLinkDelegate.dispatchFrom(this)
        finish()
    }
}
