package com.startup.studyapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: MessageEntity)

    @Query("SELECT * FROM messages ORDER BY createdAt DESC")
    fun getAll(): Flow<List<MessageEntity>>
}

@Dao
interface FlashcardDao {
    @Insert
    suspend fun insert(card: FlashcardEntity)

    @Query("SELECT * FROM flashcards ORDER BY createdAt DESC")
    fun getAll(): Flow<List<FlashcardEntity>>
}

@Dao
interface StudyDao {
    @Insert
    suspend fun insert(entry: StudyHistoryEntity)

    @Query("SELECT * FROM study_history ORDER BY createdAt DESC")
    fun getAll(): Flow<List<StudyHistoryEntity>>
}
