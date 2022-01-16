package com.kunal.pratilipi

import com.kunal.pratilipi.utils.Coroutines

class ContentRepository(
    private val appDatabase: AppDatabase
) {

    fun saveContentToDB(content:Content){
        Coroutines.io{
            appDatabase.getContentDao().saveContent(content)
        }
    }

    fun fetchContentFromDB() = appDatabase.getContentDao().getContent()

    fun deleteContent(postId:Int){
        appDatabase.getContentDao().deleteContent(postId)
    }

}