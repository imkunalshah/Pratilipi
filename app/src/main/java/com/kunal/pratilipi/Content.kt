package com.kunal.pratilipi

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "content")
data class Content(
    val postId:Int,
    val title:String,
    val description:String,
    val image:String
):Serializable{
    @PrimaryKey
    var id:Int = postId
}
