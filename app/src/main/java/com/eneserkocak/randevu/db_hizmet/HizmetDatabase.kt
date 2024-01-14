package com.eneserkocak.randevu.db_hizmet

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.eneserkocak.randevu.model.Hizmet
import com.eneserkocak.randevu.model.Personel

@Database(entities = [Hizmet::class], version = 3)

    abstract class HizmetDatabase : RoomDatabase() {
    abstract fun hizmetDao(): HizmetDao

    companion object {
        var INSTANCE: HizmetDatabase?=null

        fun getInstance(context: Context): HizmetDatabase? {
            if (INSTANCE == null) {
                synchronized(HizmetDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        HizmetDatabase::class.java, "user.db").allowMainThreadQueries().fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}