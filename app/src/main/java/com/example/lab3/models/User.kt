package com.example.lab3.models

import android.net.Uri

data class User(
    var username:String?,
    var email:String?,
    var uri: String?,
    var wordCount:String?
)

class UserConverter{
    fun convert(hashMap: HashMap<String, String>): TestUser =
        TestUser(hashMap["name"]?:"", hashMap["email"]?:"")
}