package wacode.yamada.yuki.nempaymentapp.room.goods

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Goods constructor(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val title: String,
        val price: Double
)