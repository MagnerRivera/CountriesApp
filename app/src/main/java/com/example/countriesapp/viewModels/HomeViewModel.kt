package com.example.countriesapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesapp.retrofit.Country
import com.example.countriesapp.room.CountryDao
import com.example.countriesapp.room.CountryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val countryDao: CountryDao) : ViewModel() {

    private val _countriesLiveData = MutableLiveData<List<Country>>()
    val countriesLiveData: LiveData<List<Country>> = _countriesLiveData

    fun saveCountries(countries: List<Country>) {
        viewModelScope.launch(Dispatchers.IO) {
            val countryEntities = countries.map { country ->
                CountryEntity(
                    cca3 = country.cca3,
                    name = country.name.common,
                    population = country.population,
                    status = country.status ?: "Unknown",
                    capital = country.capital?.firstOrNull() ?: "Unknown",
                    region = country.region ?: "Unknown",
                    subregion = country.subregion ?: "Unknown",
                    flags = country.flags.png ?: "Unknown",
                    startOfWeek = country.startOfWeek ?: "Unknown",
                    continents = country.continents,
                    borders = if (country.borders.isNullOrEmpty()) {
                        listOf("Unknown")
                    } else {
                        country.borders
                    }
                )
            }
            countryDao.insertCountry(countryEntities)
            _countriesLiveData.postValue(countries)
        }
    }

    fun getCountriesFromDatabase(): LiveData<List<CountryEntity>> {
        return countryDao.getAllCountries()
    }

    fun getCountryByCode(cca3: String): LiveData<CountryEntity> {
        return countryDao.getCountryByCode(cca3)
    }

}