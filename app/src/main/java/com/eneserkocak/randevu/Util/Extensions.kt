package com.eneserkocak.randevu.Util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun Date.toTarih(): Date {
    val sdf = SimpleDateFormat("dd.MM.yyyy")
    val tarih = sdf.format(this)
    return sdf.parse(tarih)
}

fun Date.toTimestamp():Timestamp {
        val cal = Calendar.getInstance()
        cal.timeInMillis = this.time
        cal.set(Calendar.SECOND,0)
        return Timestamp(cal.time)
}

fun Long.toTimestamp(): Timestamp {
    return Timestamp(Date(this))

}

fun fromLongtoDate(value: Long?): Date? {
    return value?.let { Date(it) }
}

 fun convertTimeToDate(time: Long): String {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utc.timeInMillis = time
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(utc.time)
}


fun Date.toSaat(): Date {
    val sdf = SimpleDateFormat("HH.mm")
    val saat = sdf.format(this)
    return sdf.parse(saat)
}
