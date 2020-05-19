package com.example.lab3.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.KirLatTranslator
import com.example.lab3.R
import com.example.lab3.SharedPreferencesConfig
import com.example.lab3.adapters.FavouritesAdapter
import com.example.lab3.message_samples.FavouriteMessageSample
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favourites.*
import java.util.*

class FavouritesFragment : Fragment(), FavouritesAdapter.DeleteItemListener {
    private lateinit var favouriteMessageList: MutableList<FavouriteMessageSample>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: FavouritesAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var favouriteMessage: FavouriteMessageSample
    private lateinit var sharedPreferencesConfig: SharedPreferencesConfig
    private var simpleCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            0
        ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            Collections.swap(favouriteMessageList, fromPosition, toPosition)
            recyclerViewAdapter.notifyItemMoved(fromPosition, toPosition)
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesConfig = context?.let { SharedPreferencesConfig(it) }!!
        favouriteMessageList = sharedPreferencesConfig.extractingFavouriteMessages()
        bindViews(view)
        setAdapter()
        addingNewMessage()
    }

    override fun deleteItem(position: Int, favouriteMessageSample: FavouriteMessageSample?) {
        favouriteMessage = favouriteMessageList[position]
        favouriteMessageList.removeAt(position)
        Snackbar.make(
            favouriteFragment,
            context!!.getString(
                R.string.deleted_message,
                favouriteMessage.requestedMessage,
                favouriteMessage.respondedMessage
            ),
            3000
        ).setAction(R.string.undo) {
            favouriteMessageList.add(0, favouriteMessage)
            recyclerViewAdapter.notifyItemInserted(0)
            scrollToUp()
        }.show()
        recyclerViewAdapter.notifyItemRemoved(position)
    }

    override fun onDestroy() {
        sharedPreferencesConfig.savingFavouriteMessages(favouriteMessageList)
        super.onDestroy()
    }


    private fun setAdapter() {
        recyclerViewAdapter = FavouritesAdapter(favouriteMessageList, deleteItemListener = this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun bindViews(view: View) {
        recyclerView = view.findViewById(R.id.favRecView)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun addingNewMessage() {
        favouriteMessage = sharedPreferencesConfig.extractingMessage()
        if (favouriteMessage.requestedMessage != "") {
            favouriteMessageList.add(favouriteMessage)
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun scrollToUp() {
        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = 0
        layoutManager.startSmoothScroll(smoothScroller)
    }
}