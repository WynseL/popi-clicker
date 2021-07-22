package com.wynsel.player.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Preference(private val activity: Activity) {

    companion object {
        private val KEY_TIMER_IN_MILLI = "timer_in_milli"
        private val KEY_POSITION = "position"
        private val KEY_GRID_OFFSET_X = "grid_offset_x"
        private val KEY_GRID_OFFSET_Y = "grid_offset_y"

        private var preference: Preference? = null
        fun newInstance(activity: Activity): Preference {
            if (preference == null) {
                preference = Preference(activity)
            }

            return preference!!
        }

        fun newInstance(): Preference {
            return preference!!
        }
    }

    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = activity.getSharedPreferences("popi_pref", Context.MODE_PRIVATE)
    }

    var timerInMilli: Int
        set(value) = sharedPreferences!!.edit {
            putInt(KEY_TIMER_IN_MILLI, value)
            apply()
        }
    get() = sharedPreferences!!.getInt(KEY_TIMER_IN_MILLI, 1000)

    var position: String
        set(value) = sharedPreferences!!.edit {
            putString(KEY_POSITION, value)
            apply()
        }
        get() = sharedPreferences!!.getString(KEY_POSITION, "") ?: ""

    var gridOffsetX: Float
        set(value) = sharedPreferences!!.edit {
            putFloat(KEY_GRID_OFFSET_X, value)
            apply()
        }
        get() = sharedPreferences!!.getFloat(KEY_GRID_OFFSET_X, 50f)

    var gridOffsetY: Float
        set(value) = sharedPreferences!!.edit {
            putFloat(KEY_GRID_OFFSET_Y, value)
            apply()
        }
        get() = sharedPreferences!!.getFloat(KEY_GRID_OFFSET_Y, 50f)
}