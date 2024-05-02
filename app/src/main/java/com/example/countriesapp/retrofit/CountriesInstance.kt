package com.example.countriesapp.retrofit

import com.example.countriesapp.utils.Constans
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CountriesInstance {
    private const val BASE_URL = Constans.BASE_URL

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val countryService: CountryService by lazy {
        retrofit.create(CountryService::class.java)
    }
}