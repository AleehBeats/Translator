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
import com.example.lab3.R
import com.example.lab3.adapters.FavouritesAdapter
import com.example.lab3.message_samples.FavouriteMessageSample
import java.lang.StringBuilder

class FavouritesFragment : Fragment(), FavouritesAdapter.DeleteItemListener {
    private var processedWord: String = ""
    private var receivedWord: String = ""
    private var stringBuilder: StringBuilder = StringBuilder()
    private lateinit var slicedWord: CharArray
    private var requestedMessageString: String = ""
    private var respondedMessageString: String = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var favouriteMessageList: MutableList<FavouriteMessageSample>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: FavouritesAdapter
    private lateinit var favouriteMessage: FavouriteMessageSample

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
//        extractingData()
//        sendingToAdapter()
        setAdapter()
    }

    private fun setAdapter() {
        recyclerViewAdapter = FavouritesAdapter(generatingFavMessage(), deleteItemListener=this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun bindViews(view: View) {
        recyclerView = view.findViewById(R.id.favRecView)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

//    private fun extractingData() {
//        sharedPreferences = context!!.getSharedPreferences(
//            getString(R.string.shared_preferences),
//            Context.MODE_PRIVATE
//        )
//        if (sharedPreferences.contains(getString(R.string.request_message))) {
//            requestedMessageString = sharedPreferences.getString(
//                context?.getString(R.string.request_message),
//                DEFAULT_MESSAGE
//            ).toString()
//        }
//        respondedMessageString = kirLatTranslator(requestedMessageString)
//    }

//    private fun sendingToAdapter() {
//        favouriteMessage = FavouriteMessageSample(requestedMessageString, respondedMessageString)
//        favouriteMessageList.add(favouriteMessage)
//    }

    private fun generatingFavMessage():MutableList<FavouriteMessageSample>{
        favouriteMessageList = mutableListOf()
        val messageRequest1 = "Сәлеметсізбе, қалайсыз"
        val messageResponse1 = kirLatTranslator(messageRequest1)
        val favouriteMessageSample1=FavouriteMessageSample(messageRequest1, messageResponse1)
        favouriteMessageList.add(favouriteMessageSample1)
        val messageRequest2 = "Сәлем, қалайсың"
        val messageResponse2 = kirLatTranslator(messageRequest2)
        val favouriteMessageSample2=FavouriteMessageSample(messageRequest2, messageResponse2)
        favouriteMessageList.add(favouriteMessageSample2)
        return favouriteMessageList
    }
    private fun kirLatTranslator(text: String): String {
        receivedWord = text;
        slicedWord = receivedWord.toCharArray()
        for (i: Int in slicedWord.indices)
            when (slicedWord[i]) {
                'А' -> stringBuilder.append('A')
                'а' -> stringBuilder.append('a')
                'Ә' -> stringBuilder.append('Á')
                'ә' -> stringBuilder.append('á')
                'Б' -> stringBuilder.append('B')
                'б' -> stringBuilder.append('b')
                'В' -> stringBuilder.append('V')
                'в' -> stringBuilder.append('v')
                'Г' -> stringBuilder.append('G')
                'г' -> stringBuilder.append('g')
                'Ғ' -> stringBuilder.append('Ǵ')
                'ғ' -> stringBuilder.append('ǵ')
                'Д' -> stringBuilder.append('D')
                'д' -> stringBuilder.append('d')
                'Е' -> stringBuilder.append('E')
                'е' -> stringBuilder.append('e')
                'Ё' -> stringBuilder.append("Yo")
                'ё' -> stringBuilder.append("yo")
                'Ж' -> stringBuilder.append('J')
                'ж' -> stringBuilder.append('j')
                'З' -> stringBuilder.append('Z')
                'з' -> stringBuilder.append('z')
                'И' -> stringBuilder.append('I')
                'и' -> stringBuilder.append('i')
                'Й' -> stringBuilder.append('I')
                'й' -> stringBuilder.append('i')
                'К' -> stringBuilder.append('K')
                'к' -> stringBuilder.append('k')
                'Қ' -> stringBuilder.append('Q')
                'қ' -> stringBuilder.append('q')
                'Л' -> stringBuilder.append('L')
                'л' -> stringBuilder.append('l')
                'М' -> stringBuilder.append('M')
                'м' -> stringBuilder.append('m')
                'Н' -> stringBuilder.append('N')
                'н' -> stringBuilder.append('n')
                'Ң' -> stringBuilder.append('Ń')
                'ң' -> stringBuilder.append('ń')
                'О' -> stringBuilder.append('O')
                'о' -> stringBuilder.append('o')
                'Ө' -> stringBuilder.append('Ó')
                'ө' -> stringBuilder.append('ó')
                'П' -> stringBuilder.append('P')
                'п' -> stringBuilder.append('p')
                'Р' -> stringBuilder.append('R')
                'р' -> stringBuilder.append('r')
                'С' -> stringBuilder.append('S')
                'с' -> stringBuilder.append('s')
                'Т' -> stringBuilder.append('T')
                'т' -> stringBuilder.append('t')
                'У' -> stringBuilder.append('Ý')
                'у' -> stringBuilder.append('ý')
                'Ұ' -> stringBuilder.append('U')
                'ұ' -> stringBuilder.append('u')
                'Ү' -> stringBuilder.append('Ú')
                'ү' -> stringBuilder.append('ú')
                'Ф' -> stringBuilder.append('F')
                'ф' -> stringBuilder.append('f')
                'Х' -> stringBuilder.append('H')
                'х' -> stringBuilder.append('h')
                'Һ' -> stringBuilder.append('H')
                'һ' -> stringBuilder.append('h')
                'Ц' -> stringBuilder.append("Ts")
                'ц' -> stringBuilder.append("ts")
                'Ч' -> stringBuilder.append("Ch")
                'ч' -> stringBuilder.append("ch")
                'Ш' -> stringBuilder.append("Sh")
                'ш' -> stringBuilder.append("sh")
                'Щ' -> stringBuilder.append("Shch")
                'щ' -> stringBuilder.append("shch")
                'ъ' -> stringBuilder.append('\"')
                'Ы' -> stringBuilder.append('Y')
                'ы' -> stringBuilder.append('y')
                'І' -> stringBuilder.append('I')
                'і' -> stringBuilder.append('i')
                'ь' -> stringBuilder.append('\'')
                'Э' -> stringBuilder.append('E')
                'э' -> stringBuilder.append('e')
                'Ю' -> stringBuilder.append("Yu")
                'ю' -> stringBuilder.append("yu")
                'Я' -> stringBuilder.append("Ya")
                'я' -> stringBuilder.append("ya")
                else -> {
                    stringBuilder.append(slicedWord[i])
                }
            }
        processedWord = stringBuilder.toString()
        stringBuilder.clear()
        return processedWord
    }

    override fun deleteItem(position: Int, favouriteMessageSample: FavouriteMessageSample?) {
        favouriteMessageList.removeAt(position)
        recyclerViewAdapter.notifyItemRemoved(position)
    }


}