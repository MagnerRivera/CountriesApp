package com.example.countriesapp.retrofit

import com.example.countriesapp.utils.Constans
import retrofit2.Call
import retrofit2.http.GET

interface CountryService {
    @GET(Constans.GET)
    fun getAllCountries(): Call<List<Country>>
}