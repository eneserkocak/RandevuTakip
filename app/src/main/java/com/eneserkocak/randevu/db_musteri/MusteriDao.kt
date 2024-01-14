package com.eneserkocak.randevu.db_musteri

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

import com.eneserkocak.randevu.model.Musteri
import com.eneserkocak.randevu.model.Personel

@Dao
interface MusteriDao {

    @Query("SELECT*FROM musteriler")
    fun getAll():  List<Musteri>

    @Insert
    fun insertAll(vararg musteriler: Musteri)

    @Delete
    fun delete(musteri: Musteri):Int
}