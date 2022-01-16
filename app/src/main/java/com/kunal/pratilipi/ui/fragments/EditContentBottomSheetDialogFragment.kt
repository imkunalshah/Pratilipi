package com.kunal.pratilipi.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment
import com.kunal.pratilipi.data.models.Content
import com.kunal.pratilipi.R
import com.kunal.pratilipi.utils.toast
import java.util.HashMap


class EditContentBottomSheetDialogFragment(
    private val listener: OnEditContentListener
) : RoundedBottomSheetDialogFragment() {


    interface OnEditContentListener{
        fun onEdited(content: Content)
        fun onEditCanceled()
    }
    lateinit var addImage: LinearLayout
    lateinit var postButton: LinearLayout
    lateinit var contentET: EditText
    lateinit var mediaView: RelativeLayout
    lateinit var image: ImageView
    private var imageUri: Uri? = null
    lateinit var deleteBtn: ImageButton
    lateinit var titleET: EditText
    lateinit var descET: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_content_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val h = (height * 70)/100
        view.layoutParams.height = h

        titleET = view.findViewById(R.id.titleET)
        descET = view.findViewById(R.id.contentET)
        deleteBtn = view.findViewById(R.id.delete)
        mediaView = view.findViewById(R.id.mediaView)
        image = view.findViewById(R.id.image)
        addImage = view.findViewById(R.id.imageUpload)
        postButton = view.findViewById(R.id.postButton)

        val bundle = arguments
        val content: Content = bundle?.getSerializable("content") as Content
        titleET.setText(content.title)
        descET.setText(content.description)
        imageUri = Uri.parse(content.image)
        mediaView.visibility = View.VISIBLE
        image.setImageURI(imageUri)

        if(!isReadStoragePermissionGranted() && !isWriteStoragePermissionGranted()){
            requestReadAndWriteStoragePermissions()
        }
        else if(!isReadStoragePermissionGranted()){
            requestReadStoragePermission()
        }
        else if(!isWriteStoragePermissionGranted()){
            requestWriteStoragePermission()
        }


        deleteBtn.setOnClickListener {
            imageUri = null
            image.setImageURI(null)
            mediaView.visibility = View.GONE
        }
        addImage.setOnClickListener {

            if (isWriteStoragePermissionGranted() && isReadStoragePermissionGranted()){
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 100)
            }else if(!isReadStoragePermissionGranted() && !isWriteStoragePermissionGranted()){
                requestReadAndWriteStoragePermissions()
            }
            else if(!isReadStoragePermissionGranted()){
                requestReadStoragePermission()
            }
            else if(!isWriteStoragePermissionGranted()){
                requestWriteStoragePermission()
            }
        }

        postButton.setOnClickListener {
            val title = titleET.text.toString()
            val desc = descET.text.toString()
            if (title.isNullOrEmpty() || title.isNullOrBlank()){
                context?.toast("Title is required")
            }
            else if (desc.isNullOrBlank() || desc.isNullOrEmpty()){
                context?.toast("Description is required")
            }
            else if(imageUri == null){
                context?.toast("Image is required")
            }else{
                val post = Content(content.postId,title,desc, imageUri.toString())
                listener.onEdited(post)
                context?.toast("saved!")
                dismiss()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100){
            if (data!=null){
                imageUri = data.data!!
                mediaView.visibility = View.VISIBLE
                image.setImageURI(imageUri)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            10 -> {
                val perms: HashMap<String, Int> = HashMap()
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.isNotEmpty()) {
                    for(i in permissions.indices){
                        perms[permissions[i]] = grantResults[i]
                    }
                    if (perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                        // process the normal flow

                    }else{ //else any one or both the permissions are not granted
                        when {
                            !isReadStoragePermissionGranted() && !isWriteStoragePermissionGranted() -> {
                                requestReadAndWriteStoragePermissions()
                            }
                            !isReadStoragePermissionGranted() -> {
                                requestReadStoragePermission()
                            }
                            !isWriteStoragePermissionGranted() -> {
                                requestWriteStoragePermission()
                            }
                        }
                    }
                }
            }
            18->{
                val perms: HashMap<String, Int> = HashMap()
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                if (grantResults.isNotEmpty()) {
                    for(i in permissions.indices){
                        perms[permissions[i]] = grantResults[i]
                    }
                    if (perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                        // process the normal flow

                    }else{ //else any one or both the permissions are not granted
                        requestReadStoragePermission()
                    }
                }

            }
            26->{
                val perms: HashMap<String, Int> = HashMap()
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                if (grantResults.isNotEmpty()) {
                    for(i in permissions.indices){
                        perms[permissions[i]] = grantResults[i]
                    }
                    if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                        // process the normal flow

                    }else{ //else any one or both the permissions are not granted
                        requestWriteStoragePermission()
                    }
                }
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun isWriteStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context?.applicationContext!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                Log.v("TAG", "Write Permission is revoked")
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun isReadStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context?.applicationContext!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                Log.v("TAG", "Read Permission is revoked")
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    private fun requestReadAndWriteStoragePermissions() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            10
        )
    }

    private fun requestReadStoragePermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            18
        )
    }

    private fun requestWriteStoragePermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            26
        )
    }

}