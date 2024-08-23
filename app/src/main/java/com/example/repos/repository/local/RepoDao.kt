package com.example.repos.repository.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.repos.data.ResponseMain


@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllPictures(albums: List<ResponseMain>)

    @Query("select * from repositories where page=:page")
    fun getAllPictures(page:Int): LiveData<List<ResponseMain>>

    @Query("SELECT * FROM repositories")
    fun getAllPictures(): PagingSource<Int, ResponseMain>

    @Update
    fun updateBookmark(task:ResponseMain)

    @Query("DELETE FROM repositories")
    fun clearAll()
}