package com.example.lab3.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.*
import com.example.lab3.adapters.RecyclerViewAdapter
import com.example.lab3.databases.MessageDao
import com.example.lab3.databases.MessageDatabase
import com.example.lab3.models.MessageSample
import com.example.lab3.utils.KirLatTranslator
import com.example.lab3.utils.MessageItemDecoration
import com.example.lab3.utils.SharedPreferencesConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class KirLatFragment : Fragment(), RecyclerViewAdapter.MessageClickListener,
    PopupMenu.OnMenuItemClickListener, CoroutineScope {
    private var messageString: String = ""
    private var messageId: Int = -1
    private var textEraser: String = ""

    private lateinit var sendImage: ImageView
    private lateinit var inputMessage: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var floatingActionButton: FloatingActionButton

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var messageSampleList: MutableList<MessageSample>
    private lateinit var sharedPreferencesConfig: SharedPreferencesConfig
    private lateinit var kirLatTranslator: KirLatTranslator
    private var lastVisibleItem = 0
    private var itemPosition = 0
    private val job = Job()
    private var messageDao: MessageDao? = null
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_DRAGGING -> floatingActionButton.hide()
                RecyclerView.SCROLL_STATE_IDLE -> {
                    lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    if (lastVisibleItem == recyclerViewAdapter.messageSamples?.size?.minus(1) ?: 0) {
                        floatingActionButton.hide()
                    } else {
                        floatingActionButton.show()
                    }
                }
                RecyclerView.SCROLL_STATE_SETTLING -> {

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kirlat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        messageDao = context?.let { MessageDatabase.getDatabase(it).messageDao() }
        extractingMessages()
        bindViews(view)
        setAdapter()
    }

    override fun onResume() {
        super.onResume()
        recyclerView.addOnScrollListener(scrollListener)
    }

    override fun onPause() {
        recyclerView.removeOnScrollListener(scrollListener)
        super.onPause()
    }

    override fun onDestroy() {
        recyclerViewAdapter.messageSamples?.toList()?.let { savingMessages(it) }
        super.onDestroy()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun showPopUpMenu(position: Int, itemView: View) {
        val popup = PopupMenu(context, itemView)
        itemPosition = position
        @RequiresApi(Build.VERSION_CODES.M)
        popup.gravity = Gravity.END
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.context_menu, popup.menu)
        popup.setOnMenuItemClickListener(this)
        try {
            val fieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldPopup.isAccessible = true
            val mPopup = fieldPopup.get(popup)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.e("Main", "Error showing menu icons.", e)
        } finally {
            popup.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.chCopy -> {

            }
            R.id.chShare -> {

            }
            R.id.chDelete -> {
                deletingKirLatPairs()
                return true
            }
            R.id.chAddToFavourites -> {
                addingToFavourites()
                return true
            }
        }
        return false
    }

    private fun bindViews(view: View) {
        sendImage = view.findViewById(R.id.sendImage)
        inputMessage = view.findViewById(R.id.inputText)
        recyclerView = view.findViewById(R.id.kirlatRecView)
        floatingActionButton = view.findViewById(R.id.floatingActionButton)
        floatingActionButton.hide()
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        sendImage.setOnClickListener {
            if (inputMessage.text.isNotEmpty()) {
                creatingPairMessages()
                extractingMessages()
            } else {

            }
        }
        floatingActionButton.setOnClickListener {
            scrollToBottom()
        }
        val itemDecoration = MessageItemDecoration(14, 16)
        recyclerView.addItemDecoration(itemDecoration)
    }

    private fun addingToFavourites() {
        if (itemPosition % 2 == 0)
            sharedPreferencesConfig.savingFavouriteMessage(
                messageSampleList[itemPosition],
                messageSampleList[itemPosition + 1]
            )
        else {
            sharedPreferencesConfig.savingFavouriteMessage(
                messageSampleList[itemPosition - 1],
                messageSampleList[itemPosition]
            )
        }
    }

    private fun deletingKirLatPairs() {
        if (itemPosition % 2 == 0) {
            if (itemPosition + 1 == recyclerViewAdapter.messageSamples?.size) {
                recyclerViewAdapter.messageSamples?.removeAt(itemPosition)
                recyclerViewAdapter.notifyItemRemoved(itemPosition)
            } else {
                recyclerViewAdapter.messageSamples?.removeAt(itemPosition)
                recyclerViewAdapter.notifyItemRemoved(itemPosition)
                recyclerViewAdapter.messageSamples?.removeAt(itemPosition)
                recyclerViewAdapter.notifyItemRemoved(itemPosition)
            }
        } else {
            recyclerViewAdapter.messageSamples?.removeAt(itemPosition)
            recyclerViewAdapter.notifyItemRemoved(itemPosition)
            recyclerViewAdapter.messageSamples?.removeAt(itemPosition - 1)
            recyclerViewAdapter.notifyItemRemoved(itemPosition - 1)
        }
    }

    private fun setAdapter() {
        recyclerViewAdapter =
            RecyclerViewAdapter(
                messageClickListener = this
            )
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.setHasFixedSize(true)
    }


    private fun scrollToBottom() {
        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_END
            }
        }
        smoothScroller.targetPosition = recyclerViewAdapter.messageSamples?.size?.minus(1) ?: 0
        layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun extractingMessages() {
        launch {
            val list = withContext(Dispatchers.IO) { messageDao?.getAll() }
            recyclerViewAdapter.messageSamples = list?.toMutableList()
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun savingMessages(messages: List<MessageSample>) {
        launch {
            withContext(Dispatchers.IO) {
                messageDao?.deleteAll()
                messageDao?.insertAll(messages)
            }
        }
    }

    private fun sendingMessage(messageType: MessageType, messageString: String) {
        launch {
            when (messageType) {
                MessageType.REQUEST -> {
                    withContext(Dispatchers.IO) {
                        messageId++
                        val requestMessage = MessageSample(messageId, messageString)
                        messageDao?.insertMessage(requestMessage)
                    }
                }
                MessageType.RESPONSE -> {
                    withContext(Dispatchers.IO) {
                        kirLatTranslator = KirLatTranslator()
                        messageId++
                        val responseString =
                            kirLatTranslator.kirLatTranslation(messageString)
                        val responseMessage = MessageSample(messageId, responseString)
                        messageDao?.insertMessage(responseMessage)
                    }
                }
            }
        }
    }

    private fun creatingPairMessages() {
        messageString = inputMessage.text.toString()
        inputMessage.setText(textEraser)
        sendingMessage(MessageType.REQUEST, messageString)
        sendingMessage(MessageType.RESPONSE, messageString)
    }
}