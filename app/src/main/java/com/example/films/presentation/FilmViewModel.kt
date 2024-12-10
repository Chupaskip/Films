package com.example.films.presentation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.films.FilmApplication
import com.example.films.data.network.models.search.SearchListDto
import com.example.films.data.repository.FilmRepository
import com.example.films.domain.entities.Film
import com.example.films.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(
    private val filmRepository: FilmRepository,
    app: Application
) : AndroidViewModel(app) {
    val films: MutableLiveData<Resource<SearchListDto>> = MutableLiveData()
    val film: MutableLiveData<Resource<Film>> = MutableLiveData()
    val searchText: MutableLiveData<String> = MutableLiveData()
    var disableSaveBtn:  MutableLiveData<Boolean> = MutableLiveData(false)

//    init {
//        searchText.value = getRandomWord()
//        getFilms(searchText.value ?: "")
//    }

    fun getFilms(filmName: String) {
        viewModelScope.launch {
            handleResponse(films) { filmRepository.getSearchFilms(filmName) }
        }
    }

    fun getFilm(id: String) {
        viewModelScope.launch {
            handleResponse(film) { filmRepository.getFilm(id) }
        }
    }

    private suspend fun <T> handleResponse(
        data: MutableLiveData<Resource<T>>,
        responseFun: suspend () -> Response<T>
    ) {
        data.postValue(Resource.Loading())
        try {
            if (hasInternet()) {
                val response = responseFun.invoke()
                if (response.isSuccessful) {
                    data.postValue(Resource.Success(response.body()))
                } else {
                    data.postValue(
                        Resource.Error(
                            "there are problems with api-service",
                            response.body()
                        )
                    )
                }
            } else {
                data.postValue(Resource.Error("no internet connection"))
            }
        }
       catch (t:Throwable){
           when(t){
               is IOException -> data.postValue(Resource.Error("Network Failure"))
               else -> data.postValue(Resource.Error("Conversion Error"))
           }
       }
    }

    fun hasInternet(): Boolean {
        val connectivityManager = getApplication<FilmApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return false
    }

    fun getRandomWord(): String {
        val allowedWords = listOf(
            "big",
            "rap",
            "speed",
            "trash",
            "good",
            "not bad",
            "flash",
            "boys",
            "fun",
            "town",
            "friends",
            "strange",
            "things"
        )
        return allowedWords.random()
    }

    fun upsertFilm(film: Film) {
        viewModelScope.launch {
            filmRepository.upsertFilm(film)
        }
    }

    fun deleteFilm(film: Film) {
        viewModelScope.launch {
            filmRepository.deleteFilm(film)
        }
    }

    fun getSavedFilms() = filmRepository.getSavedFilms()
}