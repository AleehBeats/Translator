package com.example.lab3.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages_table")
data class MessageSample(
    @PrimaryKey
    var id: Int,
    var messageString: String
)