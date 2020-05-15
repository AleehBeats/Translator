package com.example.lab3.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.lab3.*
import com.example.lab3.adapters.RecyclerViewAdapter
import com.example.lab3.message_samples.MessageSample
import com.google.android.material.floatingactionbutton.FloatingActionButton

class KirLatFragment : Fragment(), RecyclerViewAdapter.MessageClickListener {
    private var messageRequest: String = ""
    private var messageResponse: String = ""
    private var messageString: String = ""
    private var messageId: Int = 0
    private var textEraser: String = ""

    private lateinit var materialDialogBuilder: MaterialDialog.Builder
    private var kirLatTranslator:KirLatTranslater = KirLatTranslater()
    private lateinit var sendImage: ImageView
    private lateinit var inputMessage: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var floatingActionButton: FloatingActionButton

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var messageSampleList: MutableList<MessageSample>
    private lateinit var messageSample: MessageSample
    private lateinit var sharedPreferences: SharedPreferences

    private var lastVisibleItem = 0
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
                RecyclerView.SCROLL_STATE_SETTLING ->{

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
        sharedPreferences =
            context?.getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE
            )!!
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

    private fun setAdapter() {
        messageSampleList = mutableListOf()
        recyclerViewAdapter =
            RecyclerViewAdapter(
                messageSampleList,
                messageClickListener = this
            )
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.setHasFixedSize(true)

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
                creatingRequestMessage()
                creatingResponseMessage()

            } else {

            }
        }
        floatingActionButton.setOnClickListener {
            scrollToBottom()
        }
        val itemDecoration = MessageItemDecoration(14,16)
        recyclerView.addItemDecoration(itemDecoration)
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


    private fun creatingRequestMessage() {
        messageId++
        messageString = inputMessage.text.toString()
        messageRequest = messageString
        sendingMessageToAdapter(messageString, messageId)
        inputMessage.setText(textEraser)
    }

    private fun creatingResponseMessage() {
        messageId++
        messageString = kirLatTranslator.kirLatTranslator(messageString)
        messageResponse = messageString
        sendingMessageToAdapter(messageString, messageId)
    }

    private fun sendingMessageToAdapter(string: String, id: Int) {
        messageSample =
            MessageSample(id, string)
        messageSampleList.add(messageSample)
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun messageClick(position: Int, message: MessageSample) {
        Log.d("ClickListening", "Begin")
        materialDialogBuilder = context?.let { MaterialDialog.Builder(it) }!!
        materialDialogBuilder.title("lol").positiveText("add")
            .positiveColor(resources.getColor(R.color.darkColorAccent))
            .onPositive(MaterialDialog.SingleButtonCallback { dialog, which ->
                savingData(message.messageString)
            }).onNegative(MaterialDialog.SingleButtonCallback { dialog, which ->
            }).show()
        Log.d("ClickListening", "End")
    }

    private fun savingData(message: String) {
        val editor = sharedPreferences.edit()
        editor?.putString(context?.getString(R.string.request_message), message)
        editor?.apply()
    }


}