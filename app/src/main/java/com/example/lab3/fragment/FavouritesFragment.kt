package com.example.lab3.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.DEFAULT_MESSAGE
import com.example.lab3.KirLatTranslater
import com.example.lab3.R
import com.example.lab3.adapters.FavouritesAdapter
import com.example.lab3.message_samples.FavouriteMessageSample
import java.lang.StringBuilder

class FavouritesFragment : Fragment(), FavouritesAdapter.DeleteItemListener {
    private lateinit var favouriteMessageList: MutableList<FavouriteMessageSample>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: FavouritesAdapter
    private var kirLatTranslator:KirLatTranslater = KirLatTranslater()
    private lateinit var layoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setAdapter()
    }

    private fun setAdapter() {
        recyclerViewAdapter = FavouritesAdapter(generatingFavMessage(), deleteItemListener=this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun bindViews(view: View) {
        recyclerView = view.findViewById(R.id.favRecView)
        layoutManager=LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
    }

    private fun generatingFavMessage():MutableList<FavouriteMessageSample>{
        favouriteMessageList = mutableListOf()
        val messageRequest1 = "Сәлеметсізбе, қалайсыз"
        val messageResponse1 = kirLatTranslator.kirLatTranslator(messageRequest1)
        val favouriteMessageSample1=FavouriteMessageSample(messageRequest1, messageResponse1)
        favouriteMessageList.add(favouriteMessageSample1)
        val messageRequest2 = "Сәлем, қалайсың"
        val messageResponse2 = kirLatTranslator.kirLatTranslator(messageRequest2)
        val favouriteMessageSample2=FavouriteMessageSample(messageRequest2, messageResponse2)
        favouriteMessageList.add(favouriteMessageSample2)
        return favouriteMessageList
    }

    override fun deleteItem(position: Int, favouriteMessageSample: FavouriteMessageSample?) {
        favouriteMessageList.removeAt(position)
        recyclerViewAdapter.notifyItemRemoved(position)
    }


}