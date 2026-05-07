package com.startup.studyapp.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    private val onboardingKey = booleanPreferencesKey("onboarding_done")
    private val userNameKey = stringPreferencesKey("user_name")

    val onboardingDone: Flow<Boolean> = context.dataStore.data.map { it[onboardingKey] ?: false }
    val userName: Flow<String> = context.dataStore.data.map { it[userNameKey] ?: "Estudante" }

    suspend fun setOnboardingDone(done: Boolean) {
        context.dataStore.edit { it[onboardingKey] = done }
    }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { it[userNameKey] = name }
    }
}
