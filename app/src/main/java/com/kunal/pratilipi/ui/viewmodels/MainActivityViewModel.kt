package com.kunal.pratilipi.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kunal.pratilipi.data.models.Content
import com.kunal.pratilipi.data.repository.ContentRepository
import com.kunal.pratilipi.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    private val repository: ContentRepository,
):ViewModel() {
    lateinit var content: LiveData<List<Content>>
    fun fetchContent(){
        EventBus.getDefault().post(StartEvent())
        content = repository.fetchContentFromDB()
        EventBus.getDefault().post(SuccessEvent())
    }

    fun saveContent(content: Content){
        Coroutines.io {
            repository.saveContentToDB(content)
        }
    }

    fun editContent(content: Content){
        Coroutines.io {
            repository.saveContentToDB(content)
        }
        EventBus.getDefault().post(EditEvent())

    }
    fun deleteContent(postId:Int){
        repository.deleteContent(postId)
    }
}