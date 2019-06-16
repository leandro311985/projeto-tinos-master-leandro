package br.com.iresult.gmfmobile.utils

import android.content.Context
import android.content.SharedPreferences

enum class PreferencesKey {
    NOT_SHOW_INFORMATIVES
}

class PreferencesManager(context: Context) {

    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "APP_PREFERENCES"
    }

    fun <T>set(key: PreferencesKey, value: T) {
        val editor = sharedPreferences.edit()
        val keySp = key.name
        when (value) {
            is String -> editor.putString(keySp, value).apply()
            is Boolean -> editor.putBoolean(keySp, value).apply()
            is Int -> editor.putInt(keySp, value).apply()
            is Float -> editor.putFloat(keySp, value as Float).apply()
            else -> throw UnsupportedOperationException("Not yet implemented.")
        }
    }

    inline fun <reified T : Any> get(key: PreferencesKey, defaultValue: T? = null): T? {
        val keySp = key.name
        return when (T::class) {
            String::class -> sharedPreferences.getString(keySp, defaultValue as? String) as T?
            Int::class -> sharedPreferences.getInt(keySp, defaultValue as? Int ?: -1) as T?
            Boolean::class -> sharedPreferences.getBoolean(keySp, defaultValue as? Boolean ?: false) as T?
            Float::class -> sharedPreferences.getFloat(keySp, defaultValue as? Float ?: -1f) as T?
            Long::class -> sharedPreferences.getLong(keySp, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

//    fun remove(key: String) {
//        sharedPreferences.edit().remove(key).apply()
//    }
//
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}






