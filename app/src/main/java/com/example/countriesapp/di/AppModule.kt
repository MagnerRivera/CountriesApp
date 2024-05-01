package com.example.countriesapp.di

import android.app.Application
import androidx.room.Room
import com.example.countriesapp.room.CountriesAppDatabase
import com.example.countriesapp.room.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Proporciono la instancia única de la base de datos countriesapp-database
    @Provides
    @Singleton
    fun provideCountriesAppDatabase(application: Application): CountriesAppDatabase {
        return Room.databaseBuilder(
            application,
            CountriesAppDatabase::class.java,
            "countriesapp-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: CountriesAppDatabase): UserDao {
        return appDatabase.userDao()
    }
}