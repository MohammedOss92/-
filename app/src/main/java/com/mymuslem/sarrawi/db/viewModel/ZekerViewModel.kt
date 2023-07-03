package com.mymuslem.sarrawi.db.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mymuslem.sarrawi.MainActivity
import com.mymuslem.sarrawi.db.AzkarDB
import com.mymuslem.sarrawi.db.LocaleSource
import com.mymuslem.sarrawi.db.repository.ZekerRepository
import com.mymuslem.sarrawi.models.Letters
import com.mymuslem.sarrawi.models.ZekerTypes

//class ZekerViewModel(application: Application): AndroidViewModel(application) {
//
//    private val zekerDao = AzkarDB.getInstance(application).TypesDao()
//    private val zekerRepository = ZekerRepository(zekerDao)
//
////    private val zekerRepository: ZekerRepository = ZekerRepository(Application())
//
//    fun getAllZekerTypes():List<ZekerTypes> = zekerRepository.getZeker_Repo()
//
////    fun getAllZekerTypes():List<ZekerTypes>{
////        return zekerRepository.getAllZekerTypes()
////    }
//
//
//}
class ZekerViewModel constructor(application : Application) : ViewModel() {

    private val zekerRepository: ZekerRepository = ZekerRepository(application)


    fun getAllZekerTypes(): LiveData<List<Letters>> = zekerRepository.getAllZekerTypes()

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

