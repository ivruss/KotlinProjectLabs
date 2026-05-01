package org.example.project

import com.russhwolf.settings.Settings

class AppSettings(private val settings: Settings = Settings()) {
    
    var isDarkMode: Boolean
        get() = settings.getBoolean("is_dark_mode", false)
        set(value) = settings.putBoolean("is_dark_mode", value)

    companion object {
        private val instance = AppSettings()
        fun getInstance() = instance
    }
}
