package com.dinkar.blescanner.viewmodels

import androidx.lifecycle.*
import com.dinkar.blescanner.data.DtArea
import com.dinkar.blescanner.data.DtHistory
import com.dinkar.blescanner.data.Word
import com.dinkar.blescanner.data.WordRepository
import kotlinx.coroutines.launch

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()
    val allAreas: LiveData<List<DtArea>> = repository.allAreas.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }

    fun insertAll(words: List<Word>) = viewModelScope.launch {
        repository.insertAll(words)
    }

    fun insertHistoryAll(words: List<DtHistory>) = viewModelScope.launch {
        repository.insertHistoryAll(words)
    }

    fun insertArea(area: DtArea) = viewModelScope.launch {
        repository.insertArea(area)
    }

    fun delArea(areaname: String) = viewModelScope.launch {
        repository.delArea(areaname)
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
