package com.mymuslem.sarrawi.db.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.mymuslem.sarrawi.MainActivity
import com.mymuslem.sarrawi.db.AzkarDB
import com.mymuslem.sarrawi.db.LocaleSource
import com.mymuslem.sarrawi.db.repository.ZekerRepository
import com.mymuslem.sarrawi.models.FavoriteModel
import com.mymuslem.sarrawi.models.Letters
import com.mymuslem.sarrawi.models.ZekerTypes
import kotlinx.coroutines.launch


class ZekerViewModel constructor(application : Application) : ViewModel() {

    private var __response = MutableLiveData<List<FavoriteModel>>()
    val responseMsgsFav: MutableLiveData<List<FavoriteModel>>
        get() = __response
    private val zekerRepository: ZekerRepository = ZekerRepository(application)


    fun getAllZekerTypes(): LiveData<List<Letters>> = zekerRepository.getAllZekerTypes()

    // update msg_table items favorite state
    fun update_fav(id: Int,state:Int) = viewModelScope.launch {
        zekerRepository.update_fav(id,state)
    }

    fun getFav(): MutableLiveData<List<FavoriteModel>> {
        Log.e("tessst","entered22")
        val responseMsgsFav = MutableLiveData<List<FavoriteModel>>()

        viewModelScope.launch {
            val favList = zekerRepository.getAllFav()
            responseMsgsFav.postValue(favList)
        }

        return responseMsgsFav
    }

    fun add_fav(fav: FavoriteModel)= viewModelScope.launch {
        zekerRepository.add_fav(fav)
    }

    // delete favorite item from db
    fun delete_fav(fav: FavoriteModel)= viewModelScope.launch {
        zekerRepository.delete_fav(fav)
    }
    /*********/

    class AzkarViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ZekerViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ZekerViewModel(application) as T
            }
            throw IllegalArgumentException("Unable Constructore View Model")
        }
    }

}

