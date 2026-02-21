package com.shkhan.iqra.ui.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "fast_prefs")

class FastDataStore(private val context: Context) {

    private fun keyForDate(date: String) =
        stringPreferencesKey("fast_status_$date")

    fun getFastStatus(date: String): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[keyForDate(date)]
        }
    }

    suspend fun saveFastStatus(date: String, status: String) {
        context.dataStore.edit { prefs ->
            prefs[keyForDate(date)] = status
        }
    }
}