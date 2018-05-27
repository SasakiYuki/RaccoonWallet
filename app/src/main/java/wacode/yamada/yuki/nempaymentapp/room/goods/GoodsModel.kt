package wacode.yamada.yuki.nempaymentapp.room.goods

import android.os.Parcel
import android.os.Parcelable

data class GoodsModel(
        val id: Long,
        val title: String,
        val price: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readDouble()
    )

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.let {
            it.writeLong(id)
            it.writeString(title)
            it.writeDouble(price)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoodsModel> {
        override fun createFromParcel(p0: Parcel): GoodsModel {
            return GoodsModel(p0)
        }

        override fun newArray(p0: Int): Array<GoodsModel?> {
            return arrayOfNulls(p0)
        }

    }
}