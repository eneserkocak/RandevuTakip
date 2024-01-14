package com.eneserkocak.randevu.db_firmaMaps

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eneserkocak.randevu.model.Hizmet
import com.eneserkocak.randevu.model.Konum

@Dao
interface FirmaMapsDao {

    @Query("SELECT*FROM konum")
    fun getAll():  List<Konum>

    @Insert
    fun insertAll(vararg konum: Konum)

    @Update
    fun update(konum: Konum)

    @Delete
    fun delete(konum: Konum):Int




}