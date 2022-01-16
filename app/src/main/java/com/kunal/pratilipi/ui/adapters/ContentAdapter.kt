package com.kunal.pratilipi.ui.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.kunal.pratilipi.data.models.Content
import com.kunal.pratilipi.R
import com.kunal.pratilipi.ui.fragments.DeleteContentDialogFragment
import com.kunal.pratilipi.ui.fragments.EditContentBottomSheetDialogFragment


class ContentAdapter(
    private val context:Context,
    private val fm:FragmentManager,
    private val contentList:MutableList<Content>,
    val listener:OnEditAndDeleteListener
): RecyclerView.Adapter<ContentAdapter.ContentVH>() {

    interface OnEditAndDeleteListener{
        fun onEdit(content: Content)
        fun onDelete(id:Int)
    }
    inner class ContentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTV:TextView = itemView.findViewById(R.id.titleET)
        private val descriptionTV:TextView = itemView.findViewById(R.id.descET)
        private val image:ImageView = itemView.findViewById(R.id.image)
        private val menu:LinearLayout = itemView.findViewById(R.id.menu)
        private val editBtn:CardView = itemView.findViewById(R.id.edit)
        private val deleteBtn:CardView = itemView.findViewById(R.id.delete)
        private val shareBtn:CardView = itemView.findViewById(R.id.share)
        fun bindContent(content: Content){
            image.setImageURI(Uri.parse(content.image))
            titleTV.text = content.title
            descriptionTV.text = content.description
            itemView.setOnLongClickListener {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (menu.isVisible){
                    menu.visibility = View.GONE
                }
                else{
                    menu.visibility = View.VISIBLE
                }
                vibrator.vibrate(150L)
                true
            }

            itemView.setOnClickListener {
                if (menu.isVisible){
                    menu.visibility = View.GONE
                }
            }

            editBtn.setOnClickListener {
                val bundle = Bundle()
                val editDialog = EditContentBottomSheetDialogFragment(object : EditContentBottomSheetDialogFragment.OnEditContentListener {
                    override fun onEdited(content: Content) {
                        listener.onEdit(content)
                        notifyDataSetChanged()
                    }

                    override fun onEditCanceled() {

                    }
                })
                bundle.putSerializable("content",contentList[adapterPosition])
                editDialog.arguments = bundle
                editDialog.show(fm,"edit")
            }
            deleteBtn.setOnClickListener {
                val deleteDialog = DeleteContentDialogFragment(object : DeleteContentDialogFragment.OnDeleteContentListener {
                    override fun onContentDelete() {
                        listener.onDelete(content.postId)
                        contentList.removeAt(adapterPosition)
                        notifyDataSetChanged()
                    }

                    override fun onContentDeleteCancelled() {

                    }
                })
                deleteDialog.isCancelable = false
                deleteDialog.show(fm,"delete")
            }

            shareBtn.setOnClickListener {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_TEXT, content.title)
                shareIntent.putExtra(Intent.EXTRA_STREAM, content.image)
                shareIntent.type = "image/jpeg"
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.startActivity(Intent.createChooser(shareIntent, "Share"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentVH {
        return ContentVH(LayoutInflater.from(context).inflate(R.layout.layout_content, parent, false))
    }

    override fun onBindViewHolder(holder: ContentVH, position: Int) {
        holder.bindContent(contentList[position])
    }

    override fun getItemCount(): Int = contentList.size

    fun updateContent(list:MutableList<Content>){
        contentList.clear()
        contentList.addAll(list)
        notifyDataSetChanged()
    }
}