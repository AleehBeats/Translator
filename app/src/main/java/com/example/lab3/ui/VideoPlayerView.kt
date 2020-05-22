package com.example.lab3.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.example.lab3.R
import kotlinx.android.synthetic.main.interests_item.view.*
import kotlinx.android.synthetic.main.video_player_view.view.*
import org.w3c.dom.Text
import java.util.ArrayList
class VideoPlayerView
@JvmOverloads constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int = 0) :
    FrameLayout(context, attributeSet, defStyleAttr) {
    private lateinit var currentTimedText: TextView
    private lateinit var maxDurationTimeText: TextView
    private lateinit var playButtonImage: ImageView
    private lateinit var dashText: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var interfaceView: MutableList<View>
    private var isSeekBarVisible = false
    private lateinit var runnable: Runnable
    private var handlerView: Handler = Handler()
    private var view: View = inflate(context, R.layout.video_player_view, this)

    init {
        bindViews(view)
        attributeSet?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.VideoPlayerView, defStyleAttr, 0)
            isSeekBarVisible = typedArray.getBoolean(
                R.styleable.VideoPlayerView_isSeekBarVisible,
                isSeekBarVisible
            )
            seekBar.isVisible = isSeekBarVisible
            typedArray.recycle()
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                videoView.seekTo(seekBar?.progress ?: 0)
                videoView.start()
            }

        })
        preparingVideo()
    }

    private fun bindViews(view: View) {
        interfaceView = mutableListOf()
        currentTimedText = view.findViewById(R.id.currentTimeText)
        maxDurationTimeText = view.findViewById(R.id.maxDurationTimeText)
        playButtonImage = view.findViewById(R.id.playButtonImage)
        dashText = view.findViewById(R.id.dashText)
        seekBar = view.findViewById(R.id.seekBar)
        interfaceView.add(currentTimedText)
        interfaceView.add(maxDurationTimeText)
        interfaceView.add(playButtonImage)
        interfaceView.add(dashText)
        interfaceView.add(seekBar)
    }

    fun hidingViews() {
        for (view in interfaceView) {
            view.isVisible = false
        }
    }

    fun showingViews() {
        for (view in interfaceView) {
            view.isVisible = true
        }
    }

    fun bindingUrl(url: String?) {
        videoView.setVideoPath(url)
    }

    fun playingVideo() {
        hidingViews()
        videoView.start()
    }

    fun pausingVideo() {
        showingViews()
        videoView.pause()
    }

    private fun listeningPlayerTrack() {
        runnable = Runnable {
            seekBar.progress = videoView.currentPosition
            currentTimedText.text = creatingTimeLabel(seekBar.progress)
            handlerView.postDelayed(runnable, 100)
        }
        handlerView.postDelayed(runnable, 100)
    }

    private fun preparingVideo(){
        videoView.setOnPreparedListener {
            val totTime = creatingTimeLabel(it.duration)
            maxDurationTimeText.text = totTime
            seekBar.max = it.duration
            listeningPlayerTrack()
        }
    }

    private fun creatingTimeLabel(duration: Int): String {
        var timeLabel = ""
        val min = duration / 1000 / 60
        val sec = duration / 1000 % 60
        timeLabel += "$min:"
        if (sec < 10)
            timeLabel += "0"
        timeLabel += sec
        return timeLabel
    }

    fun destroy() {
        handlerView.removeCallbacks(runnable)
    }
}

