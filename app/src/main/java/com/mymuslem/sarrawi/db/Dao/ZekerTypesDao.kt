package com.mymuslem.sarrawi.db.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mymuslem.sarrawi.models.Letters
import com.mymuslem.sarrawi.models.ZekerTypes

@Dao
interface ZekerTypesDao {

    @Query("select * from one")
    fun getAllZekerTypesDao(): LiveData<List<Letters>>

//    @Insert
//    suspend fun insert_zekerTypes (zekerTypes: ZekerTypes)

}