package com.example.lab3.databases

class MovieDatabase {
    val videoLinks:MutableList<String> = mutableListOf()
    init{
        chargingMovieBase()
    }
    private fun chargingMovieBase(){
        videoLinks.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
        videoLinks.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        videoLinks.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        videoLinks.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        videoLinks.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4")
        videoLinks.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4")
        videoLinks.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4")
        videoLinks.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4")
        videoLinks.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4")
    }
}