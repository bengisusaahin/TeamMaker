package com.yusuf.navigation.main_datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.myPreferencesDataStore: DataStore<Preferences> by preferencesDataStore( "settings")

@Singleton
class MainDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val myPreferencesDataStore = context.myPreferencesDataStore

    private object PreferencesKeys {
        val APP_ENTRY_KEY = booleanPreferencesKey("app_entry")
        val SHOW_TOOLTIP_KEY = booleanPreferencesKey("show_tooltip")
    }

    val readAppEntry = myPreferencesDataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[PreferencesKeys.APP_ENTRY_KEY] ?: true
    }

    val readShowTooltip = myPreferencesDataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[PreferencesKeys.SHOW_TOOLTIP_KEY] ?: true
    }

    suspend fun saveAppEntry() {
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_ENTRY_KEY] = false
        }
    }

    suspend fun saveShowTooltip(show: Boolean) {
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_TOOLTIP_KEY] = show
        }
    }
}
