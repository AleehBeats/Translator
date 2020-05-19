package com.example.lab3

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.message_samples.MessageSample

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener {
    abstract fun bind(messageSample: MessageSample?)
}