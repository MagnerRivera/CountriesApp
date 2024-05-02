package com.example.countriesapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): User?
}

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCountry(country: List<CountryEntity>)

    @Query("SELECT * FROM countries")
    fun getAllCountries(): LiveData<List<CountryEntity>>

    @Query("SELECT * FROM countries WHERE cca3 = :cca3")
    fun getCountryByCode(cca3: String): LiveData<CountryEntity>

    @Query("SELECT * FROM countries WHERE cca3 IN (:codes)")
    suspend fun getCountriesByCodes(codes: List<String>): List<CountryEntity>

}