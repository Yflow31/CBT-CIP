package com.approval.unitconvertor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.approval.unitconvertor.data.MainPageData

class ItemAdapter(
    private val context: Context,
    private val itemList: List<MainPageData>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.main_page_item_text)
        val imageView: ImageView = itemView.findViewById(R.id.main_page_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_for_each_page, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textView.text = currentItem.text
        holder.imageView.setImageResource(currentItem.imageResourceId)

        // Set click listener for the item to start a new activity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, currentItem.activity)
            context.startActivity(intent)
        }
    }
}
