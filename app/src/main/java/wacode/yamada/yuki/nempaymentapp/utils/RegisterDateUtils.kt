package wacode.yamada.yuki.nempaymentapp.utils

import android.content.Context
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object RegisterDateUtils {
    private const val SP_REGISTER_DATE = "sp_register_date"
    private const val SP_FORMATTER = "yyyy/MM/dd HH:mm:ss.SSS"
    private const val registerInitDate = ""
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(SP_FORMATTER)

    fun saveRegisterDate(context: Context) {
        val registerDate = LocalDateTime.now()
        val formattedString = registerDate.format(dateTimeFormatter)
        SharedPreferenceUtils.put(context, SP_REGISTER_DATE, formattedString)
    }

    fun isThreeDaysLater(context: Context): Boolean {
        val now = LocalDateTime.now()
        val registerDateString = SharedPreferenceUtils[context, SP_REGISTER_DATE, registerInitDate]
        return if (registerDateString == registerInitDate) {
            saveRegisterDate(context)
            false
        } else {
            val dateTime = LocalDateTime.parse(registerDateString, dateTimeFormatter)
            val between = Duration.between(now, dateTime)
            between.toDays() <= ReviewAppealUtils.REVIEW_APPEAL_DAYS
        }
    }
}