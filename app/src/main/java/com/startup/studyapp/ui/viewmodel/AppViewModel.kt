package com.startup.studyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.startup.studyapp.data.local.FlashcardEntity
import com.startup.studyapp.data.local.MessageEntity
import com.startup.studyapp.data.local.StudyHistoryEntity
import com.startup.studyapp.data.repository.StudyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(private val repository: StudyRepository) : ViewModel() {
    val messages: StateFlow<List<MessageEntity>> = repository.getMessages().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val flashcards: StateFlow<List<FlashcardEntity>> = repository.getFlashcards().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val history: StateFlow<List<StudyHistoryEntity>> = repository.getHistory().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val onboardingDone: StateFlow<Boolean> = repository.onboardingDone.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val userName: StateFlow<String> = repository.userName.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Estudante")

    fun askTutor(prompt: String) = viewModelScope.launch { repository.askTutor(prompt) }
    fun summarize(text: String, onReady: (String) -> Unit) = viewModelScope.launch { onReady(repository.summarize(text)) }
    fun addFlashcard(question: String, answer: String) = viewModelScope.launch { repository.addFlashcard(question, answer) }
    fun finishOnboarding(name: String) = viewModelScope.launch { repository.finishOnboarding(name) }
}

class AppViewModelFactory(private val repository: StudyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
