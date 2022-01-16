package com.kunal.pratilipi.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.kunal.pratilipi.R

class DeleteContentDialogFragment(
    val listener: OnDeleteContentListener
) : DialogFragment() {

    interface OnDeleteContentListener{
        fun onContentDelete()
        fun onContentDeleteCancelled()
    }

    lateinit var delete:AppCompatButton
    lateinit var goBack:AppCompatButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_content_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val newWidth = (width * 80)/100
        view.layoutParams.width = newWidth
        delete = view.findViewById(R.id.delete)
        goBack = view.findViewById(R.id.goBack)
        delete.setOnClickListener {
            listener.onContentDelete()
            dismiss()
        }
        goBack.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_fm)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

}