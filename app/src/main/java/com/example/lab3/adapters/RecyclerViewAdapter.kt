package com.example.lab3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.*
import com.example.lab3.message_samples.MessageSample

class RecyclerViewAdapter(
    var messageSamples: MutableList<MessageSample>?,
    val messageClickListener: MessageClickListener? = null
) : RecyclerView.Adapter<BaseViewHolder>() {
    private lateinit var messageView: View
    private lateinit var messageTextView: TextView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        return if (viewType == REQUEST_MESSAGE_VIEW_TYPE) {
            messageView = LayoutInflater.from(parent.context)
                .inflate(R.layout.request_message_layout, parent, false)
            RequestMessageHolder(messageView)
        } else {
            messageView = LayoutInflater.from(parent.context)
                .inflate(R.layout.response_message_layout, parent, false)
            ResponseMessageHolder(messageView)
        }
    }

    override fun getItemCount(): Int {
        return messageSamples?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            REQUEST_MESSAGE_VIEW_TYPE
        } else {
            RESPONSE_MESSAGE_VIEW_TYPE
        }
    }

//    fun addingMessage(message: MessageSample) {
//        messageSamples?.add(message)
//        notifyDataSetChanged()
//    }

    inner class RequestMessageHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(messageSample: MessageSample?) {
            messageTextView = itemView.findViewById(R.id.requestMessage)
            if (messageSample != null) {
                messageTextView.text = messageSample.messageString
                itemView.setOnClickListener{
                    messageClickListener?.messageClick(adapterPosition, messageSample)
                }
            }
        }
    }

    inner class ResponseMessageHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(messageSample: MessageSample?) {
            messageTextView = itemView.findViewById(R.id.responseMessage)
            if (messageSample != null) {
                messageTextView.text = messageSample.messageString
                itemView.setOnClickListener {
                    messageClickListener?.messageClick(adapterPosition, messageSample)
                }
            }
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(messageSamples?.get(position))
    }
    interface MessageClickListener{
        fun messageClick(position: Int, message: MessageSample)
    }
}