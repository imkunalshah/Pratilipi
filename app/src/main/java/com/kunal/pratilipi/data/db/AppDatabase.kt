package com.kunal.pratilipi.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kunal.pratilipi.data.models.Content

@Database(
    entities = [Content::class],
    version = 1
)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getContentDao(): ContentDao

    companion object{

        @Volatile
        private var instance: AppDatabase? =  null

        private val LOCK = Any()

        operator fun invoke(context:Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "App.db"
            ).allowMainThreadQueries().build()
    }
}