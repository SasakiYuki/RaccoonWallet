package wacode.yamada.yuki.nempaymentapp.utils

import com.ryuta46.nemkotlin.model.HarvestInfo
import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble
import wacode.yamada.yuki.nempaymentapp.extentions.getNemStartDateTimeLong
import wacode.yamada.yuki.nempaymentapp.model.HarvestAppEntity
import java.text.SimpleDateFormat
import java.util.*

object HarvestAppConverter {
    fun convert(harvestInfo: HarvestInfo) = HarvestAppEntity(
            convertDate(harvestInfo.timeStamp),
            convertFee(harvestInfo.totalFee)
    )

    private fun convertDate(timeStamp: Int): String {
        val nemStartTimeLong = getNemStartDateTimeLong()
        val sdf = SimpleDateFormat("MM/dd yyyy")
        val date = Date(timeStamp.toLong() * 1000 + nemStartTimeLong)
        return sdf.format(date)
    }

    private fun convertFee(totalFee: Long) = totalFee.convertNEMFromMicroToDouble().toString()

}