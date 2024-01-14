package com.eneserkocak.randevu.db_hizmet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eneserkocak.randevu.model.Hizmet
import com.eneserkocak.randevu.model.Personel

@Dao
interface HizmetDao {

    @Query("SELECT*FROM hizmetler")
    fun getAll():  List<Hizmet>

    @Insert
    fun insertAll(vararg hizmetler: Hizmet)


    @Delete
    fun delete(hizmet: Hizmet):Int



}