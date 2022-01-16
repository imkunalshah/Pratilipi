package com.kunal.pratilipi.data.repository

import com.kunal.pratilipi.data.models.Content
import com.kunal.pratilipi.data.db.AppDatabase
import com.kunal.pratilipi.utils.Coroutines

class ContentRepository(
    private val appDatabase: AppDatabase
) {

    fun saveContentToDB(content: Content){
        Coroutines.io{
            appDatabase.getContentDao().saveContent(content)
        }
    }

    fun fetchContentFromDB() = appDatabase.getContentDao().getContent()

    fun deleteContent(postId:Int){
        appDatabase.getContentDao().deleteContent(postId)
    }

}