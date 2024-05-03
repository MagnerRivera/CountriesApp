package com.example.countriesapp.utils

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun toStringList(value: List<String>): String {
        return value.joinToString(",")
    }
}