package com.example.countriesapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class CountriesAppDatabase : RoomDatabase()  {

    abstract fun userDao(): UserDao

}