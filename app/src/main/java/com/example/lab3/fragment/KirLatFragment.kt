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
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.lab3.*
import com.example.lab3.adapters.FavouritesAdapter
import com.example.lab3.adapters.RecyclerViewAdapter
import com.example.lab3.message_samples.FavouriteMessageSample
import com.example.lab3.message_samples.MessageSample
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.StringBuilder
import java.lang.reflect.Type
import kotlin.reflect.typeOf

class KirLatFragment : Fragment(), RecyclerViewAdapter.MessageClickListener {
    private var receivedWord: String = ""
    private var processedWord: String = ""
    private var messageRequest: String = ""
    private var messageResponse: String = ""
    private var messageString: String = ""
    private var favMessageString: String = ""
    private var favMessageStringTranslated: String = ""
    private var messageId: Int = 0
    private var textEraser: String = ""

    private lateinit var slicedWord: CharArray
    private lateinit var materialDialogBuilder: MaterialDialog.Builder
    private var stringBuilder: StringBuilder = StringBuilder()

    private lateinit var sendImage: ImageView
    private lateinit var inputMessage: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    private lateinit var messageSampleList: MutableList<MessageSample>
    private lateinit var favouriteMessageList: MutableList<FavouriteMessageSample>
    private lateinit var messageSample: MessageSample
    private lateinit var favouriteMessage: FavouriteMessageSample
    private lateinit var sharedPreferences: SharedPreferences


    private val gson: Gson = Gson()
    private var json=""
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


    private fun setAdapter() {
        messageSampleList = mutableListOf()
        recyclerViewAdapter =
            RecyclerViewAdapter(
                messageSampleList,
                messageClickListener = this
            )
        recyclerView.adapter = recyclerViewAdapter
    }

    private fun bindViews(view: View) {
        sendImage = view.findViewById(R.id.sendImage)
        inputMessage = view.findViewById(R.id.inputText)
        recyclerView = view.findViewById(R.id.kirlatRecView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        sendImage.setOnClickListener {
            if (inputMessage.text.isNotEmpty()) {
                creatingRequestMessage()
                creatingResponseMessage()

            } else {

            }
        }
    }

    private fun collectingFavouriteMessages(requestMessage: String){
        favMessageString=requestMessage
        favMessageStringTranslated=kirLatTranslator(favMessageString)
        favouriteMessage=FavouriteMessageSample(favMessageString, favMessageStringTranslated)
        favouriteMessageList.add(favouriteMessage)
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
        messageString = kirLatTranslator(messageString)
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
            .positiveColor(resources.getColor(R.color.labelColor))
            .onPositive(MaterialDialog.SingleButtonCallback { dialog, which ->
                savingData(message.messageString)
            }).onNegative(MaterialDialog.SingleButtonCallback { dialog, which ->
            }).show()
        Log.d("ClickListening", "End")
    }

    private fun savingData(message:String) {
        val editor=sharedPreferences.edit()
        editor?.putString(context?.getString(R.string.request_message), message)
        editor?.apply()
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


}