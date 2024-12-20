package com.example.films.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.films.domain.entities.Film
import com.example.films.domain.repository.FilmRepository
import com.example.films.domain.usecases.AddFilmToFavoritesUseCase
import com.example.films.domain.usecases.DeleteFilmFromFavoritesUseCase
import com.example.films.domain.usecases.GetFavoritesFilmsUseCase
import com.example.films.domain.usecases.GetSearchFilmsUseCase
import com.example.films.util.Resource
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

    val searchFilmsState: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val favoriteFilms = getSavedFilmsUseCase()
    val filmState: MutableLiveData<Resource<Film>> = MutableLiveData()
    val searchText: MutableLiveData<String> = MutableLiveData()
    var disableSaveBtn: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        searchText.value = getRandomWord()
    }

    fun getFilms(filmName: String) {
        viewModelScope.launch {
            searchFilmsState.value = Resource.Loading()
            val result = getSearchFilmsUseCase(filmName)
            searchFilmsState.value = result
        }
    }

    fun getFilm(id: String) {
        viewModelScope.launch {
            filmState.value = Resource.Loading()
            val result = filmRepository.getFilm(id)
            filmState.value = result
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

    private fun getRandomWord(): String {
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
}