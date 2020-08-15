package com.quicklycoding.rocketkotlin

import android.content.Context
import androidx.preference.PreferenceManager

class PreferenceProvider(context: Context) {

    private val preference = PreferenceManager.getDefaultSharedPreferences(context)

    //Setters
    fun setInt(key: String, value: Int) = preference.edit().putInt(key, value).apply()
    fun setString(key: String, value: String) = preference.edit().putString(key, value).apply()
    fun setLong(key: String, value: Long) = preference.edit().putLong(key, value).apply()
    fun setBoolean(key: String, value: Boolean) = preference.edit().putBoolean(key, value).apply()

    //Getter
    fun getInt(key: String) = preference.getInt(key, -1)
    fun getString(key: String) = preference.getString(key, null)
    fun getLong(key: String) = preference.getLong(key, 0)
    fun getBoolean(key: String) = preference.getBoolean(key, false)

    //Check Is Key Exists or not
    fun isContains(key: String) = preference.contains(key)

}