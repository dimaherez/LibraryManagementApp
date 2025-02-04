package com.example.librarymanagementapp.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.enums.Genre
import com.example.domain.models.Book
import com.example.librarymanagementapp.LibraryDB
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class ViewModelMVVM: ViewModel() {

    private val _data = MutableLiveData<List<Book>>()
    val data = _data as LiveData<List<Book>>

    private val _loading = MutableLiveData<Boolean>()
    val loading = _loading as LiveData<Boolean>

    private val _createLoading = MutableLiveData<Boolean>()
    val createLoading = _createLoading as LiveData<Boolean>

    private val _error = MutableLiveData<String>()
    val error = _error as LiveData<String>


    init {
        fetchBooks()
    }

    fun fetchBooks() {
        _loading.postValue(true)
        viewModelScope.launch {

            val response = LibraryDB.loadBooks()

            if (response != null){
                _data.postValue(response!!)
            } else {
                _data.postValue(emptyList())
                _error.postValue("Error while fetching books")
            }

            _loading.postValue(false)
        }
    }

    fun sortBooks() {
        val currentList = _data.value?.toList()
        _data.value = currentList?.sortedBy { it.title }
    }

    fun filterBooks() {
        val currentList = _data.value?.toList()
        _data.value = currentList?.filter { it.genre == Genre.FANTASY }
    }

    fun createBook(book: Book) {
        viewModelScope.launch {
            Log.d("mylog", "${book.title} Start")
            _createLoading.value = true
            delay(Random.nextLong(100, 3000))

            if (_data.value.isNullOrEmpty().not())
                _data.value = _data.value?.plus(book)
            // If these lines move under LibraryDB.addBook, invalid result occurs
            // (sometimes the same book is adding to _data.value twice)

            LibraryDB.addBook(book)

            _createLoading.value = false
            Log.d("mylog", "${book.title} End")
        }
    }



    // only for signed in users
//    fun fetchUser() {
//        if (isUserSignedIn.not()) return
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = LibraryDB.loadUsers()
//
//            if (response.isNotEmpty()){
//                _loading.value = false
//                _dataUsers.value = response!!
//            } else {
//                _loading.value = false
//                _dataUsers.value = emptyList()
//            }
//
//        }
//    }
}