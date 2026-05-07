package com.startup.studyapp.data.repository

import com.startup.studyapp.data.local.FlashcardDao
import com.startup.studyapp.data.local.FlashcardEntity
import com.startup.studyapp.data.local.MessageDao
import com.startup.studyapp.data.local.MessageEntity
import com.startup.studyapp.data.local.StudyDao
import com.startup.studyapp.data.local.StudyHistoryEntity
import com.startup.studyapp.datastore.SettingsDataStore

class StudyRepository(
    private val studyDao: StudyDao,
    private val messageDao: MessageDao,
    private val flashcardDao: FlashcardDao,
    private val settingsDataStore: SettingsDataStore
) {
    fun getMessages() = messageDao.getAll()
    fun getFlashcards() = flashcardDao.getAll()
    fun getHistory() = studyDao.getAll()
    val onboardingDone = settingsDataStore.onboardingDone
    val userName = settingsDataStore.userName

    suspend fun askTutor(prompt: String): String {
        val response = "Resposta simulada: Para entender '$prompt', divida o tema em conceito, exemplo e revisão em 24h."
        messageDao.insert(MessageEntity(prompt = prompt, response = response))
        studyDao.insert(StudyHistoryEntity(type = "Tutor", content = prompt))
        return response
    }

    suspend fun summarize(text: String): String {
        val short = text.split(".").take(2).joinToString(". ").take(280)
        val result = if (short.isBlank()) "Resumo vazio. Cole um texto maior." else "Resumo: $short"
        studyDao.insert(StudyHistoryEntity(type = "Resumo", content = result))
        return result
    }

    suspend fun addFlashcard(question: String, answer: String) {
        flashcardDao.insert(FlashcardEntity(question = question, answer = answer))
        studyDao.insert(StudyHistoryEntity(type = "Flashcard", content = question))
    }

    suspend fun finishOnboarding(name: String) {
        settingsDataStore.setUserName(name)
        settingsDataStore.setOnboardingDone(true)
    }
}
