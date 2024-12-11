package com.example.films.di

import android.content.Context
import androidx.room.Room
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.films.R
import com.example.films.data.database.FilmDao
import com.example.films.data.database.FilmDatabase
import com.example.films.data.network.FilmsApi
import com.example.films.data.network.RetrofitInstance
import com.example.films.data.repository.FilmRepositoryImpl
import com.example.films.domain.repository.FilmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
            .baseUrl(RetrofitInstance.BASE_URL)
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

    @Provides
    @Singleton
    fun provideGlide(
        @ApplicationContext context:Context
    ):RequestManager {
        val drawable = CircularProgressDrawable(context)
        drawable.setColorSchemeColors(R.color.purple_200,
            R.color.purple_500,
            R.color.purple_700)
        drawable.centerRadius = 30f
        drawable.strokeWidth = 5f
        drawable.start()
        return Glide.with(context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .error(R.drawable.ic_broken_image)
                    .placeholder(drawable)
            )
    }

    @Provides
    @Singleton
    fun provideFilmRepository(api: FilmsApi, dao: FilmDao): FilmRepository =
        FilmRepositoryImpl(dao, api)
}