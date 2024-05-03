package com.example.countriesapp.retrofit

import com.example.countriesapp.utils.Constants
import retrofit2.Call
import retrofit2.http.GET

interface CountryService {
    @GET(Constants.GET)
    fun getAllCountries(): Call<List<Country>>
}