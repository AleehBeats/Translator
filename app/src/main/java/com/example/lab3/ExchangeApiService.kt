package com.example.lab3

import com.example.lab3.message_samples.TranslatedMessage
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val key =
    "trnsl.1.1.20200524T102729Z.ade3b538609460ba.45635739d3950dc38b85595992876b142f31c4f2"
const val baseUrl = "https://translate.yandex.net/"

interface ExchangeApiService {
    companion object {
        fun create(): ExchangeApiService {
            val retrofit =
                Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl).build()
            return retrofit.create(ExchangeApiService::class.java)
        }
    }
    @GET("api/v1.5/tr.json/translate")
    fun getTranslate(
        @Query("key") key: String,
        @Query("text") text: String,
        @Query("lang") lang: String
    ): Single<TranslatedMessage>
}