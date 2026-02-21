package com.shkhan.iqra.ui.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shkhan.iqra.ui.model.RamadanDay
import java.io.InputStreamReader

class RamadanRepository(private val context: Context) {

    fun loadRamadanData(): List<RamadanDay> {
        val inputStream = context.assets.open("ramadan_1447_bangalore.json")
        val reader = InputStreamReader(inputStream)
        val listType = object : TypeToken<List<RamadanDay>>() {}.type
        return Gson().fromJson(reader, listType)
    }
}