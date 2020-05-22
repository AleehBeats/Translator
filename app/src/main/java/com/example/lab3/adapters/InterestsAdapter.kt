package com.example.lab3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.R
import com.example.lab3.ui.VideoPlayerView
import kotlinx.android.synthetic.main.interests_item.view.*
import kotlinx.android.synthetic.main.video_player_view.view.*

class InterestsAdapter(
    var videoLinks:MutableList<String>?,
    var videoClickListener: VideoClickListener? =null
) : RecyclerView.Adapter<InterestsAdapter.InterestsViewHolder>() {
    private lateinit var videoView: View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestsViewHolder {
        videoView = LayoutInflater.from(parent.context).inflate(R.layout.interests_item, parent, false)
        return InterestsViewHolder(videoView)
    }

    override fun getItemCount(): Int {
        return videoLinks?.size ?: 0
    }

    override fun onBindViewHolder(holder: InterestsViewHolder, position: Int) {
        holder.bind(videoLinks?.get(position))
    }

    inner class InterestsViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        fun bind(url:String?){
            videoView = itemView.findViewById(R.id.videoViewItem)
            videoView.videoViewItem.bindingUrl(url)
            videoView.videoViewItem.playButtonImage.setOnClickListener {
                videoClickListener?.playButtonClick(adapterPosition, itemView)
            }
            videoView.videoViewItem.setOnClickListener{
                videoClickListener?.videoItemClicked(adapterPosition, itemView)
            }
        }
    }

    interface VideoClickListener{
        fun playButtonClick(position:Int, itemView: View)
        fun videoItemClicked(position:Int, itemView: View)
    }
}

