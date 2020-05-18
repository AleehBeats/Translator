package com.example.lab3.message_samples

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageSample(
    @PrimaryKey
    var id: Int,
    var messageString: String
)