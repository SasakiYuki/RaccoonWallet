package wacode.yamada.yuki.nempaymentapp.utils


import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import java.text.SimpleDateFormat
import java.util.*

class DateComparator : Comparator<TransactionAppEntity> {
    override fun compare(transactionAppEntity: TransactionAppEntity, t1: TransactionAppEntity): Int {
        val sdf = SimpleDateFormat("MM/dd,yyyy k:mm;ss")

        val t1Date = sdf.parse(transactionAppEntity.date)
        val t2Date = sdf.parse(t1.date)

        return t2Date.compareTo(t1Date)
    }
}
