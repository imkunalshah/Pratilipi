package com.kunal.pratilipi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import com.kunal.pratilipi.data.models.Content
import com.kunal.pratilipi.ui.fragments.NewPostBottomSheetDialogFragment
import com.kunal.pratilipi.R
import com.kunal.pratilipi.ui.adapters.ContentAdapter
import com.kunal.pratilipi.ui.fragments.QuitDialogFragment
import com.kunal.pratilipi.ui.viewmodels.MainActivityViewModel
import com.kunal.pratilipi.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private val viewModel: MainActivityViewModel by viewModels()
    lateinit var contentList:MutableList<Content>
    lateinit var rvAdapter: ContentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contentList = ArrayList()
        newPost.setOnClickListener {
            val dialog = NewPostBottomSheetDialogFragment(object : NewPostBottomSheetDialogFragment.OnNewPostListener{
                override fun onSave(content: Content) {
                    viewModel.saveContent(content)
                }
            })
            dialog.show(supportFragmentManager,"new_post")
        }

        searchET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! >= 3){
                    if (this@MainActivity::contentList.isInitialized && this@MainActivity::rvAdapter.isInitialized){
                        filter(p0.toString().lowercase())
                    }
                }else{
                    viewModel.fetchContent()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        viewModel.fetchContent()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSuccessEvent(event: SuccessEvent){
        viewModel.content.observe(this){
            contentList.clear()
            contentList.addAll(it.toMutableList())
            rvAdapter = ContentAdapter(this@MainActivity,supportFragmentManager,it.toMutableList(),object : ContentAdapter.OnEditAndDeleteListener{
                override fun onEdit(content: Content) {
                    viewModel.editContent(content)
                }

                override fun onDelete(id: Int) {
                    viewModel.deleteContent(id)
                }
            })
            rv_content.apply {
                adapter = rvAdapter
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFailEvent(event: FailEvent){
        root.snackBar(event.message)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEditEvent(event: EditEvent){
        viewModel.fetchContent()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStartEvent(event: StartEvent){

    }


    override fun onBackPressed() {
        val quitDialog = QuitDialogFragment()
        val fm: FragmentManager = this.supportFragmentManager
        quitDialog.show(fm,"quit")
    }

    fun filter(text: String) {
        val temp: MutableList<Content> = ArrayList()
        for (d in contentList) {
            if (d.title.lowercase().contains(text)) {
                temp.add(d)
            }
        }
        //update recyclerview
        rvAdapter.updateContent(temp)
    }
}