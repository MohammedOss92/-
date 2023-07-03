package com.mymuslem.sarrawi.db.repository


import android.app.Application
import androidx.lifecycle.LiveData
import com.mymuslem.sarrawi.db.AzkarDB
import com.mymuslem.sarrawi.db.Dao.ZekerTypesDao
import com.mymuslem.sarrawi.db.LocaleSource
import com.mymuslem.sarrawi.models.Letters
import com.mymuslem.sarrawi.models.ZekerTypes

class ZekerRepository(app:Application) {

    private var zekerTypesDao:ZekerTypesDao
    private var allZeker: LiveData<List<Letters>>


    init{
        val database = AzkarDB.getInstance(app)
        zekerTypesDao = database.getTypesDao()
        allZeker = zekerTypesDao.getAllZekerTypesDao()
    }

    fun getAllZekerTypes(): LiveData<List<Letters>> {
        return zekerTypesDao.getAllZekerTypesDao()
    }
}