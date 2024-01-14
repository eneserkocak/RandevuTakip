package com.eneserkocak.randevu.db_musteri

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eneserkocak.randevu.model.Musteri

@Database(entities = [Musteri::class], version = 4)

abstract class MusteriDatabase : RoomDatabase() {
    abstract fun musteriDao(): MusteriDao

    companion object {
        var INSTANCE: MusteriDatabase?=null

        fun getInstance(context: Context): MusteriDatabase? {
            if (INSTANCE == null) {
                synchronized(MusteriDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        MusteriDatabase::class.java, "user.db").allowMainThreadQueries().fallbackToDestructiveMigration()
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