package com.startup.studyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.startup.studyapp.data.local.AppDatabase
import com.startup.studyapp.data.repository.StudyRepository
import com.startup.studyapp.datastore.SettingsDataStore
import com.startup.studyapp.ui.navigation.AppNavHost
import com.startup.studyapp.ui.theme.StudyStartupTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(applicationContext)
        val repository = StudyRepository(
            studyDao = database.studyDao(),
            messageDao = database.messageDao(),
            flashcardDao = database.flashcardDao(),
            settingsDataStore = SettingsDataStore(applicationContext)
        )

        setContent {
            StudyStartupTheme {
                AppNavHost(repository = repository)
            }
        }
    }
}
