package com.example.lab3.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databases.MovieDatabase
import com.example.lab3.R
import com.example.lab3.adapters.InterestsAdapter
import kotlinx.android.synthetic.main.interests_item.view.*
import kotlinx.android.synthetic.main.video_player_view.view.*

class InterestsFragment : Fragment(), InterestsAdapter.VideoClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var interestsAdapter: InterestsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var movieDatabase: MovieDatabase
    private var isPlaying: Boolean = false
    private var isShowing: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_interests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDatabase = MovieDatabase()
        bindViews(view)
        setAdapter()
    }

    private fun bindViews(view: View) {
        recyclerView = view.findViewById(R.id.interestsRecView)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
    }

    private fun setAdapter() {
        interestsAdapter = InterestsAdapter(movieDatabase.videoLinks, videoClickListener = this)
        recyclerView.adapter = interestsAdapter
        interestsAdapter.notifyDataSetChanged()
    }


    override fun playButtonClick(position: Int, itemView: View) {
        isPlaying = if (!isPlaying) {
            itemView.videoViewItem.playingVideo()
            itemView.videoViewItem.playButtonImage.setImageResource(R.drawable.ic_pauseimage)
            true
        } else {
            itemView.videoViewItem.pausingVideo()
            itemView.videoViewItem.playButtonImage.setImageResource(R.drawable.ic_playimage)
            false
        }
    }

    override fun videoItemClicked(position: Int, itemView: View) {
        isShowing = if (isShowing){
            itemView.videoViewItem.hidingViews()
            false
        } else{
            itemView.videoViewItem.showingViews()
            true
        }
    }
}
