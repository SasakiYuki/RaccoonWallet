package wacode.yamada.yuki.nempaymentapp.extentions

import android.app.Activity
import android.support.v4.app.ActivityCompat

fun Activity.getColorCompat(colorResourceId: Int) = ActivityCompat.getColor(this, colorResourceId)
