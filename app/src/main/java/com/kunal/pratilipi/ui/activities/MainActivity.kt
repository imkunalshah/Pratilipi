package com.kunal.pratilipi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import com.kunal.pratilipi.Content
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newPost.setOnClickListener {
            val dialog = NewPostBottomSheetDialogFragment(object : NewPostBottomSheetDialogFragment.OnNewPostListener{
                override fun onSave(content: Content) {
                    viewModel.saveContent(content)
                }
            })
            dialog.show(supportFragmentManager,"new_post")
        }
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
            rv_content.apply {
                adapter = ContentAdapter(this@MainActivity,supportFragmentManager,it.toMutableList(),object : ContentAdapter.OnEditAndDeleteListener{
                    override fun onEdit(content: Content) {
                        viewModel.editContent(content)
                    }

                    override fun onDelete(id: Int) {
                        viewModel.deleteContent(id)
                    }
                })
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
}