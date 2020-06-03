package com.example.lab3.databases

import androidx.room.*
import com.example.lab3.models.MessageSample

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list:List<MessageSample>)

    @Query("SELECT * FROM messages_table")
    fun getAll():List<MessageSample>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(messageSample: MessageSample)

    @Query("DELETE FROM messages_table")
    fun deleteAll()
}