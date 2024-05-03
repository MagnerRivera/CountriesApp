package com.example.countriesapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.countriesapp.utils.Converters

@Database(entities = [User::class, CountryEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class CountriesAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun countryDao(): CountryDao
}