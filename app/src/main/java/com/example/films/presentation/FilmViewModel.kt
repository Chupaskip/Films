package com.example.films.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.films.domain.entities.Film
import com.example.films.domain.entities.SearchFilm
import com.example.films.domain.repository.FilmRepository
import com.example.films.domain.usecases.AddFilmToFavoritesUseCase
import com.example.films.domain.usecases.DeleteFilmFromFavoritesUseCase
import com.example.films.domain.usecases.GetFavoritesFilmsUseCase
import com.example.films.domain.usecases.GetSearchFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(
    private val filmRepository: FilmRepository,
) : ViewModel() {
    private val getSearchFilmsUseCase = GetSearchFilmsUseCase(filmRepository)
    private val addFilmToFavoritesUseCase = AddFilmToFavoritesUseCase(filmRepository)
    private val deleteFilmFromFavoritesUseCase = DeleteFilmFromFavoritesUseCase(filmRepository)
    private val getSavedFilmsUseCase = GetFavoritesFilmsUseCase(filmRepository)

    val searchFilms: MutableLiveData<List<SearchFilm>> = MutableLiveData()
    val favoriteFilms = getSavedFilmsUseCase()
    val film: MutableLiveData<Film> = MutableLiveData()
    val searchText: MutableLiveData<String> = MutableLiveData()
    var disableSaveBtn:  MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        searchText.value = getRandomWord()
        getFilms(searchText.value ?: "")
    }

    fun getFilms(filmName: String) {
        viewModelScope.launch {
            searchFilms.value = getSearchFilmsUseCase(filmName)
        }
    }

    fun getFilm(id: String) {
        viewModelScope.launch {
            film.value = filmRepository.getFilm(id)
        }
    }

    fun addFilmToFavorites(film: Film) {
        viewModelScope.launch {
            addFilmToFavoritesUseCase(film)
        }
    }

    fun deleteFilmFromFavorites(film: Film) {
        viewModelScope.launch {
            deleteFilmFromFavoritesUseCase(film)
        }
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

//    private suspend fun <T> handleResponse(
//        data: MutableLiveData<Resource<T>>,
//        responseFun: suspend () -> Response<T>
//    ) {
//        data.postValue(Resource.Loading())
//        try {
//            if (hasInternet()) {
//                val response = responseFun.invoke()
//                if (response.isSuccessful) {
//                    data.postValue(Resource.Success(response.body()))
//                } else {
//                    data.postValue(
//                        Resource.Error(
//                            "there are problems with api-service",
//                            response.body()
//                        )
//                    )
//                }
//            } else {
//                data.postValue(Resource.Error("no internet connection"))
//            }
//        }
//       catch (t:Throwable){
//           when(t){
//               is IOException -> data.postValue(Resource.Error("Network Failure"))
//               else -> data.postValue(Resource.Error("Conversion Error"))
//           }
//       }
//    }
//
//    fun hasInternet(): Boolean {
//        val connectivityManager = getApplication<FilmApplication>().getSystemService(
//            Context.CONNECTIVITY_SERVICE
//        ) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val activeNetwork = connectivityManager.activeNetwork ?: return false
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//            return when {
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                else -> false
//            }
//        }
//        return false
//    }
}