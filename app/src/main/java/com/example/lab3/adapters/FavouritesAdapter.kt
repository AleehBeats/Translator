package com.example.lab3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.message_samples.FavouriteMessageSample
import com.example.lab3.R
import java.util.*

class FavouritesAdapter(
    var favouriteMessages: MutableList<FavouriteMessageSample>?,
    val deleteItemListener: DeleteItemListener? = null
) : RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {
    private lateinit var requestedMessage: TextView
    private lateinit var respondedMessage: TextView
    private lateinit var deleteImage: ImageView
    private lateinit var favouriteMessageView: View
    private lateinit var dragImage: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        favouriteMessageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_message, parent, false)
        return FavouritesViewHolder(favouriteMessageView)
    }

    override fun getItemCount(): Int {
        return favouriteMessages?.size ?: 0
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.bind(favouriteMessages?.get(position))
    }

//    fun addFavouriteMessage(favouriteMessageSample: FavouriteMessageSample){
//        favouriteMessages?.add(favouriteMessageSample)
//        notifyDataSetChanged()
//    }
    inner class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favouriteMessageSample: FavouriteMessageSample?) {
            requestedMessage = itemView.findViewById(R.id.requestedMessage)
            respondedMessage = itemView.findViewById(R.id.responsedMessage)
            deleteImage = itemView.findViewById(R.id.deleteImage)
            dragImage=itemView.findViewById(R.id.dragImage)
            requestedMessage.text = favouriteMessageSample?.requestedMessage
            respondedMessage.text = favouriteMessageSample?.respondedMessage
            deleteImage.setOnClickListener {
                deleteItemListener?.deleteItem(adapterPosition,favouriteMessageSample)
            }
        }
    }

    interface DeleteItemListener {
        fun deleteItem(position: Int, favouriteMessageSample: FavouriteMessageSample?)
    }
}