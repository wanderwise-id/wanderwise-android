package com.example.wanderwise.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.wanderwise.data.PostRepository
import com.example.wanderwise.data.local.database.CityFavorite
import com.example.wanderwise.data.preferences.UserModel
import com.example.wanderwise.utils.Event
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel (private val pRepository: PostRepository): ViewModel() {

    fun getSessionUser(): LiveData<UserModel> {
        return pRepository.getSession().asLiveData()
    }

    fun insert(cityFavorite: CityFavorite){
        pRepository.insertCityFav(cityFavorite)
    }

    fun getAllCity() = pRepository.getAllFav()

    fun getClickedCity(keyCity: String): LiveData<CityFavorite> = pRepository.getClickedCity(keyCity)

    fun delete(cityFavorite: CityFavorite){
        pRepository.deleteCityFav(cityFavorite)
    }

    fun editUserModel(userModel: UserModel) {
        viewModelScope.launch {
            pRepository.saveSession(userModel)
        }
    }

    var locationData: String? = ""
    var safetyScore: Any? = null

    val sharedData = MutableLiveData<String>()

}