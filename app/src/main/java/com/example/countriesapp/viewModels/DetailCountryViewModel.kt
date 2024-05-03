package com.example.countriesapp.viewModels

import androidx.lifecycle.ViewModel
import com.example.countriesapp.room.CountryDao
import com.example.countriesapp.room.CountryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailCountryViewModel @Inject constructor(private val countryDao: CountryDao) : ViewModel() {

    suspend fun getBorderCountriesInfo(borders: List<String>): List<CountryEntity> {
        return countryDao.getCountriesByCodes(borders)
    }
}