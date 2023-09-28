package me.nomi.urdutyper.data.source

import android.content.Context
import android.content.SharedPreferences
import me.nomi.urdutyper.domain.repository.SharedPreferenceRepository
import me.nomi.urdutyper.domain.utils.Constants

class SharedPreference(context: Context) : SharedPreferenceRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences(Constants.PREFS_FILENAME, 0)

    override var uid: String
        get() = prefs.getString(Constants.USER_ID, "") ?: ""
        set(value) = prefs.edit().putString(Constants.USER_ID, value).apply()
//
//
//
//    var billers: List<Biller>?
//        get() {
//            val jsonStr = prefs.getString(BILLERSS, null)
//            if (jsonStr != null) {
//                val itemType = object : TypeToken<List<Biller>>() {}.type
//                return Gson().fromJson(jsonStr, itemType)
//            } else {
//                return null
//            }
//        }
//        set(value) {
//            val jsonStr: String
//            if (value != null) {
//                jsonStr = Gson().toJson(value)
//                prefs.edit().putString(BILLERSS, jsonStr).apply()
//            }
//        }

    override fun clear() {
        prefs.edit().clear().apply()
    }
}