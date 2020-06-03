package com.example.lab3.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.utils.BaseViewHolder
import com.example.lab3.R
import com.example.lab3.utils.REQUEST_MESSAGE_VIEW_TYPE
import com.example.lab3.utils.RESPONSE_MESSAGE_VIEW_TYPE
import com.example.lab3.models.MessageSample

class TranslateAdapter(
    var messageSamples: MutableList<MessageSample>?,
    var messageClickListener: MessageClickListener
) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private lateinit var messageTextView: TextView
    private lateinit var messageView: View
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        return if (viewType == REQUEST_MESSAGE_VIEW_TYPE) {
            messageView =
                LayoutInflater.from(context).inflate(R.layout.translate_request_message, parent, false)
            RequestViewHolder(messageView)
        } else {
            messageView =
                LayoutInflater.from(context).inflate(R.layout.translate_response_message, parent, false)
            ResponseViewHolder(messageView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            REQUEST_MESSAGE_VIEW_TYPE
        } else {
            RESPONSE_MESSAGE_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return messageSamples?.size ?: 0
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(messageSamples?.get(position))
    }

    inner class RequestViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(messageSample: MessageSample?) {
            messageTextView = itemView.findViewById(R.id.translateRequestMessage)
            if (messageSample != null) {
                messageTextView.text = messageSample.messageString
                itemView.setOnLongClickListener(this)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            messageClickListener.showPopUpMenu(adapterPosition, itemView)
            return true
        }

    }

    inner class ResponseViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(messageSample: MessageSample?) {
            messageTextView = itemView.findViewById(R.id.translateResponseMessage)
            if (messageSample != null) {
                messageTextView.text = messageSample.messageString
                itemView.setOnLongClickListener(this)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            messageClickListener.showPopUpMenu(adapterPosition, itemView)
            return true
        }

    }

    interface MessageClickListener {
        fun showPopUpMenu(position: Int, itemView: View)
    }
}