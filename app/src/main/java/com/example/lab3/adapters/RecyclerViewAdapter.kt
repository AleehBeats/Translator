package com.example.lab3.adapters

import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.*
import com.example.lab3.message_samples.MessageSample
import com.example.lab3.utils.BaseViewHolder
import com.example.lab3.utils.REQUEST_MESSAGE_VIEW_TYPE
import com.example.lab3.utils.RESPONSE_MESSAGE_VIEW_TYPE

class RecyclerViewAdapter(
    var messageSamples: MutableList<MessageSample>?=null,
    val messageClickListener: MessageClickListener? = null
) : RecyclerView.Adapter<BaseViewHolder>() {
    private lateinit var messageView: View
    private lateinit var messageTextView: TextView
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        return if (viewType == REQUEST_MESSAGE_VIEW_TYPE) {
            messageView = LayoutInflater.from(parent.context)
                .inflate(R.layout.kirlat_request_message, parent, false)
            RequestMessageHolder(messageView)
        } else {
            messageView = LayoutInflater.from(parent.context)
                .inflate(R.layout.kirlat_response_message, parent, false)
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


    inner class RequestMessageHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(messageSample: MessageSample?) {
            messageTextView = itemView.findViewById(R.id.requestMessage)
            if (messageSample != null) {
                messageTextView.text = messageSample.messageString
                itemView.setOnLongClickListener(this)
            }
        }


        override fun onLongClick(v: View?): Boolean {
            messageClickListener?.showPopUpMenu(adapterPosition, itemView)
            return true
        }


    }

    inner class ResponseMessageHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(messageSample: MessageSample?) {
            messageTextView = itemView.findViewById(R.id.responseMessage)
            if (messageSample != null) {
                messageTextView.text = messageSample.messageString
                itemView.setOnLongClickListener(this)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            messageClickListener?.showPopUpMenu(adapterPosition, itemView)
            return true
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(messageSamples?.get(position))
    }

    interface MessageClickListener {
        fun showPopUpMenu(position: Int, itemView: View)
    }
}


