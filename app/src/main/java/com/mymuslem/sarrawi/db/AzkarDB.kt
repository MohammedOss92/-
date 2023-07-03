package com.mymuslem.sarrawi.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mymuslem.sarrawi.db.Dao.ZekerTypesDao
import com.mymuslem.sarrawi.models.Letters
import com.mymuslem.sarrawi.models.ZekerTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Letters::class], version = 5, exportSchema = false)
abstract class AzkarDB : RoomDatabase() {

    abstract fun getTypesDao(): ZekerTypesDao

    companion object {
        @Volatile
        private var instance: AzkarDB? = null

        fun getInstance(con: Context): AzkarDB {
            if (instance == null) {
                synchronized(AzkarDB::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            con.applicationContext,
                            AzkarDB::class.java,
//                            "aa.db"
                            "aa.db"
                        )
                            .fallbackToDestructiveMigration()
                            .createFromAsset("database/aa.db")
                            .build()
                    }
                }
            }
            return instance!!
        }

    }
}


//    companion object {
//        @Volatile
//        private var instance: AzkarDB? = null
//
//        fun getInstance(con: Context): AzkarDB {
//            if (instance == null) {
//
//                    instance =
//                        Room.databaseBuilder(con, AzkarDB::class.java, "hesenmoslem.db")
//                            .addCallback(object : Callback() {
//                                override fun onCreate(db: SupportSQLiteDatabase) {
//                                    super.onCreate(db)
//                                    Log.d("hesenmoslemDatabase", "populating with data...")
//                                    GlobalScope.launch(Dispatchers.IO) { rePopulateDb(instance) }
//                                }
//                            })
//
//                            .build()
//                }
//                return instance!!
//
//            }
//        }
//    }




