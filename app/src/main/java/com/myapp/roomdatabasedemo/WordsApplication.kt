package com.myapp.roomdatabasedemo

import android.app.Application
import com.myapp.roomdatabasedemo.data.WordRepository
import com.myapp.roomdatabasedemo.data.WordRoomDatabase

class WordsApplication : Application() {
    val database by lazy { WordRoomDatabase.getDatabase(this) }
    val repository by lazy { WordRepository(database.wordDao()) }
}
