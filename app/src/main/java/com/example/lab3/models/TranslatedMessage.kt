package com.example.lab3.models

data class TranslatedMessage (
    var code:Int,
    var lang:String,
    var text:List<String>
)