package com.example.countriesapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): User?
}