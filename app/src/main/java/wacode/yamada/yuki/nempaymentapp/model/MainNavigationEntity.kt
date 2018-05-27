package wacode.yamada.yuki.nempaymentapp.model

import android.graphics.drawable.Drawable
import wacode.yamada.yuki.nempaymentapp.types.MainNavigationType

data class MainNavigationEntity(val text:String,val drawable:Drawable, val isHeader:Boolean = false, val isFooter:Boolean = false,val type:MainNavigationType)