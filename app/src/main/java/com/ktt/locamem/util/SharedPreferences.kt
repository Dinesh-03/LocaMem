package com.ktt.locamem.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {

    fun addData(context: Context, preferenceName: String, key: String, value: Any) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                else -> error("Only primitive types can be stored in SharedPreferences")
            }
        }.apply()
    }

    fun getString(context: Context, preferenceName: String, key: String): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, Constants.EMPTY)!!
    }

    fun getStringSet(context: Context, preferenceName: String, key: String): MutableSet<String> {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet(key, setOf())!!
    }

    fun getInt(context: Context, preferenceName: String, key: String): Int {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key, 0)
    }

    fun getBoolean(context: Context, preferenceName: String, key: String): Boolean {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }

    fun getLong(context: Context, preferenceName: String, key: String): Long {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(key, 0L)
    }

    fun getFloat(context: Context, preferenceName: String, key: String): Float {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getFloat(key, 0.0F)
    }

}