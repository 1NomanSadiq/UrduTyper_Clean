package me.nomi.urdutyper.data.source

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import me.nomi.urdutyper.domain.entity.User
import me.nomi.urdutyper.utils.Constants


class SharedPreference(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(Constants.PREFS_FILENAME, 0)

        var uid: String
        get() = prefs.getString(Constants.USER_ID, "")?: ""
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

    fun clear() {
        prefs.edit().clear().apply()
    }
}