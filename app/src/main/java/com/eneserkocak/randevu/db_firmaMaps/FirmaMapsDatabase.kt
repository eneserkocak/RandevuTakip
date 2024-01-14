package com.eneserkocak.randevu.db_firmaMaps

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eneserkocak.randevu.db_hizmet.HizmetDao
import com.eneserkocak.randevu.db_hizmet.HizmetDatabase
import com.eneserkocak.randevu.model.Hizmet
import com.eneserkocak.randevu.model.Konum


@Database(entities = [Konum::class], version = 2)
    abstract class FirmaMapsDatabase : RoomDatabase() {
    abstract fun firmaMapsDao(): FirmaMapsDao

    companion object {
        var INSTANCE: FirmaMapsDatabase?=null

        fun getInstance(context: Context): FirmaMapsDatabase? {
            if (INSTANCE == null) {
                synchronized(FirmaMapsDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FirmaMapsDatabase::class.java, "user.db").allowMainThreadQueries().fallbackToDestructiveMigration()
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
