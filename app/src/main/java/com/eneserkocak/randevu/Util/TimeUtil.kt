package com.eneserkocak.randevu.Util

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.eneserkocak.randevu.model.Zaman
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.Clock
import java.util.*

object TimeUtil{

    //GUNLER RECYCLER ADAPTER İÇİNDE "BAŞLANGIÇ" VE "BİTİŞ" SAATLERİNİ AŞAĞIDA Kİ TİMEPİCKER ı KULLANARAK ALACAĞIZ..

    fun showTimerPickerFragment(context: Context,parentFragmentManager:FragmentManager,onCompleted:(Zaman)->Unit) {
        val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()


        materialTimePicker.show(parentFragmentManager, "MainActivity")
            materialTimePicker.addOnPositiveButtonClickListener {
                onCompleted.invoke(Zaman(materialTimePicker.hour,materialTimePicker.minute))

            }




        }


    }




