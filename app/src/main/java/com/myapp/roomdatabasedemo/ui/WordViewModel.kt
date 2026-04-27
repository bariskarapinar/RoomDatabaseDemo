package com.myapp.roomdatabasedemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.myapp.roomdatabasedemo.data.Word
import com.myapp.roomdatabasedemo.data.WordRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    val allWords: StateFlow<List<Word>> = repository.allWords.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }

    fun update(word: Word) = viewModelScope.launch {
        repository.update(word)
    }

    fun delete(word: Word) = viewModelScope.launch {
        repository.delete(word)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
