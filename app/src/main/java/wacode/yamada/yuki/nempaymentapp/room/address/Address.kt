package wacode.yamada.yuki.nempaymentapp.room.address

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import wacode.yamada.yuki.nempaymentapp.extentions.getId
import wacode.yamada.yuki.nempaymentapp.model.PaymentQREntity

@Entity
data class Address constructor(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val address: String,
        val name: String
) {
    companion object {
        fun convert(entity: PaymentQREntity) = Address(id = getId(), address = entity.data.addr, name = entity.data.name ?: "")
    }
}
