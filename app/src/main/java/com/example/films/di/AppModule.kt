package com.example.films.di

import android.content.Context
import androidx.room.Room
import com.example.films.database.FilmDatabase
import com.example.films.network.FilmsApi
import com.example.films.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideFilmsApi(retrofit: Retrofit): FilmsApi =
        retrofit.create(FilmsApi::class.java)

    @Provides
    @Singleton
    fun provideFilmDataBase(@ApplicationContext app: Context): FilmDatabase = Room.databaseBuilder(
        app,
        FilmDatabase::class.java,
        "film_db.db"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideFilmDao(db: FilmDatabase) = db.getFilmDao()
}