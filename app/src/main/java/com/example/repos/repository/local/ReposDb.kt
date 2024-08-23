package com.example.repos.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.repos.data.Owner
import com.example.repos.data.RemoteKeys
import com.example.repos.data.ResponseMain


@Database(
    entities = [ResponseMain::class, Owner::class, RemoteKeys::class],
    version = 1,
    exportSchema = true
)
abstract class ReposDb : RoomDatabase() {
    abstract fun reposDao(): RepoDao
    abstract fun remoteDao():RemoteKeysDao

    companion object {
        @Volatile
        private var instance: ReposDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            ReposDb::class.java, "Repos.db"
        ).allowMainThreadQueries().build()
    }
}