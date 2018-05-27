package wacode.yamada.yuki.nempaymentapp.utils


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SharedPreferenceUtils {
    private var preferences: SharedPreferences? = null

    private fun getEditor(context: Context): SharedPreferences.Editor {
        return getPref(context).edit()
    }

    private fun getPref(context: Context): SharedPreferences {
        preferences = if (preferences != null) preferences else PreferenceManager.getDefaultSharedPreferences(context)
        return preferences!!
    }

    fun put(context: Context, prefName: String, value: String) {
        getEditor(context).putString(prefName, value).apply()
    }

    fun put(context: Context, prefName: String, value: Long) {
        getEditor(context).putLong(prefName, value).apply()
    }

    fun put(context: Context, prefName: String, value: Int?) {
        getEditor(context).putInt(prefName, value!!).apply()
    }

    fun put(context: Context, prefName: String, value: Double?) {
        getEditor(context).putString(prefName, value.toString()).apply()
    }

    fun put(context: Context, prefName: String, value: Boolean?) {
        getEditor(context).putBoolean(prefName, value!!).apply()
    }

    operator fun get(context: Context, prefName: String, defaultValue: String?): String {
        return getPref(context).getString(prefName, defaultValue)
    }

    operator fun get(context: Context, prefName: String, defaultValue: Int?): Int {
        return getPref(context).getInt(prefName, defaultValue!!)
    }

    operator fun get(context: Context, prefName: String, defaultValue: Boolean?): Boolean {
        return getPref(context).getBoolean(prefName, defaultValue!!)
    }

    operator fun get(context: Context, prefName: String, defaultValue: Long?): Long {
        return getPref(context).getLong(prefName, defaultValue!!)
    }

    operator fun get(context: Context, prefName: String, defaultValue: Double?): Double {
        return try {
            getPref(context).getString(prefName, defaultValue.toString()).toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            0.0
        }
    }

    fun remove(context: Context, prefName: String) {
        getEditor(context).remove(prefName).commit()
    }
}
