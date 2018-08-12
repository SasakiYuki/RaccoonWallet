package wacode.yamada.yuki.nempaymentapp.utils

import android.net.Uri
import com.google.firebase.dynamiclinks.PendingDynamicLinkData

object DynamicLinkParser {
    fun checkDynamicLinkType(pendingDynamicLinkData: PendingDynamicLinkData): Type {
        pendingDynamicLinkData.link.toString().let {
            return if (it.contains("payment")) {
                Type.PAYMENT
            } else {
                Type.HOME
            }
        }
    }

    fun getUri(pendingDynamicLinkData: PendingDynamicLinkData): Uri? {
        pendingDynamicLinkData.link?.let {
            return it
        } ?: return null
    }

    enum class Type {
        HOME,
        PAYMENT,
        NONE
    }
}