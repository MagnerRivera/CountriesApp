package com.example.countriesapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val password: String,
    val username: String,
    val email: String,
)

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val cca3: String,
    val name: String,
    val population: Int,
    val status: String,
    val capital: String,
    val region: String,
    val subregion: String,
    val flags: String,
    val startOfWeek: String,
    val continents: List<String>,
    val borders: List<String>
)