package wacode.yamada.yuki.nempaymentapp.room.address

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity data class MyAddress constructor(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val walletInfoId: Long
)