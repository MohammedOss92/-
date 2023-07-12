package com.mymuslem.sarrawi.db.Dao

import androidx.room.*
import com.mymuslem.sarrawi.models.FavoriteModel
@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add_fav(fav: FavoriteModel)

    //    @Query("Select * from Favorite_table")
    @Query("select * from Fav order by ID DESC")
    suspend fun getAllFav(): List<FavoriteModel>

    // delete favorite item from db
    @Delete
    suspend fun deletefav(item:FavoriteModel)
}