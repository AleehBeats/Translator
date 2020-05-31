package com.example.lab3.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab3.message_samples.MessageSample

@Database(entities = [MessageSample::class], version = 1)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        var INSTANCE: MessageDatabase? = null
        fun getDatabase(context: Context): MessageDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MessageDatabase::class.java,
                    "message_database.db"
                ).build()
            }
            return INSTANCE!!
        }
    }


}