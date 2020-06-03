package com.example.lab3.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.ExchangeApiService
import com.example.lab3.utils.KirLatTranslator
import com.example.lab3.utils.MessageItemDecoration
import com.example.lab3.R
import com.example.lab3.utils.SharedPreferencesConfig
import com.example.lab3.adapters.TranslateAdapter
import com.example.lab3.key
import com.example.lab3.models.MessageSample
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TranslateFragment : Fragment(), TranslateAdapter.MessageClickListener,
    PopupMenu.OnMenuItemClickListener {
    private var messageRequest: String = ""
    private var messageString: String = ""
    private var messageId: Int = 0
    private var textEraser: String = ""

    private lateinit var sendImage: ImageView
    private lateinit var inputMessage: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: TranslateAdapter
    private lateinit var floatingActionButton: FloatingActionButton

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var messageSampleList: MutableList<MessageSample>
    private lateinit var sharedPreferencesConfig: SharedPreferencesConfig

    private val exchangeApiService = ExchangeApiService.create()
    private val disposable = CompositeDisposable()
    private var languageDirection: String = ""
    private lateinit var kirLatTranslator: KirLatTranslator
    private lateinit var toolbar: Toolbar
    private var lastVisibleItem = 0
    private var itemPosition = 0
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_DRAGGING -> floatingActionButton.hide()
                RecyclerView.SCROLL_STATE_IDLE -> {
                    lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    if (lastVisibleItem == messageSampleList.size - 1) {
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
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        sharedPreferencesConfig = context?.let {
            SharedPreferencesConfig(
                it
            )
        }!!
        messageSampleList = sharedPreferencesConfig.extractingTranslatedMessages()
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
        sharedPreferencesConfig.savingTranslatedMessages(messageSampleList)
        super.onDestroy()
        disposable.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.translate_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.chEngRus -> {
                languageDirection = "en-ru"
                return true
            }
            R.id.chRusEng -> {
                languageDirection = "ru-en"
                return true
            }
        }
        return false
    }
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
            messageSampleList.removeAt(itemPosition)
            recyclerViewAdapter.notifyItemRemoved(itemPosition)
            messageSampleList.removeAt(itemPosition)
            recyclerViewAdapter.notifyItemRemoved(itemPosition)
        } else {
            messageSampleList.removeAt(itemPosition)
            recyclerViewAdapter.notifyItemRemoved(itemPosition)
            messageSampleList.removeAt(itemPosition - 1)
            recyclerViewAdapter.notifyItemRemoved(itemPosition - 1)
        }
    }

    private fun scrollToBottom() {
        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_END
            }
        }
        smoothScroller.targetPosition = messageSampleList.size - 1
        layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun bindViews(view: View) {
        sendImage = view.findViewById(R.id.translateSendImage)
        inputMessage = view.findViewById(R.id.translateInputText)
        recyclerView = view.findViewById(R.id.translateRecView)
        floatingActionButton = view.findViewById(R.id.translateFloatingButton)
        floatingActionButton.hide()
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        sendImage.setOnClickListener {
            if (inputMessage.text.isNotEmpty()) {
                creatingRequestMessage()

            } else {

            }
        }
        floatingActionButton.setOnClickListener {
            scrollToBottom()
        }
        val itemDecoration = MessageItemDecoration(14, 16)
        recyclerView.addItemDecoration(itemDecoration)
    }

    private fun setAdapter() {
        recyclerViewAdapter =
            TranslateAdapter(
                messageSampleList,
                messageClickListener = this
            )
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.setHasFixedSize(true)

    }


    private fun creatingRequestMessage() {
        messageString = inputMessage.text.toString()
        messageRequest = messageString
        val requestMessage = MessageSample(messageId, messageString)
        sendingMessageToAdapter(requestMessage)
        creatingResponseMessage(requestMessage, languageDirection)
        inputMessage.setText(textEraser)
    }

    private fun creatingResponseMessage(requestMessage: MessageSample, langDirection: String) {
        exchangeApiService.getTranslate(key, requestMessage.messageString, langDirection)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                val id = requestMessage.id++
                val responseMessage = MessageSample(id, it.text[0])
                sendingMessageToAdapter(responseMessage)
            }, {
                Toast.makeText(context, "Message not delivered", Toast.LENGTH_SHORT).show()
            }).also { disposable }
    }

    private fun sendingMessageToAdapter(messageSample: MessageSample) {
        messageSampleList.add(messageSample)
        recyclerViewAdapter.notifyDataSetChanged()
    }

}