package com.kunal.pratilipi.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kunal.pratilipi.data.models.Content

@Dao
interface ContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveContent(content: Content) : Long

    @Query("SELECT * FROM content")
    fun getContent() : LiveData<List<Content>>

    @Query("DELETE FROM content where id = :postId")
    fun deleteContent(postId:Int)

}