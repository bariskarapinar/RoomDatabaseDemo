package com.myapp.roomdatabasedemo.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {

    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    @WorkerThread
    suspend fun update(word: Word) {
        wordDao.update(word)
    }

    @WorkerThread
    suspend fun delete(word: Word) {
        wordDao.delete(word)
    }

    @WorkerThread
    suspend fun deleteAll() {
        wordDao.deleteAll()
    }
}
