package com.kunal.pratilipi.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kunal.pratilipi.data.models.Content
import java.lang.reflect.Type


class DataConverter {

    @TypeConverter
    fun fromContentList(content: List<Content?>?): String? {
        if (content == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Content?>?>() {}.type
        return gson.toJson(content, type)
    }

    @TypeConverter
    fun toContentList(content: String?): List<Content>? {
        if (content == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Content?>?>() {}.type
        return gson.fromJson<List<Content>>(content, type)
    }

}