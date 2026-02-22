package com.shkhan.iqra.ui.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shkhan.iqra.ui.model.Dua
import java.io.InputStreamReader

class DuaRepository(context: Context) {

    private val duas: List<Dua>

    init {
        val inputStream = context.assets.open("dua.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Dua>>() {}.type
        duas = Gson().fromJson(reader, type)
    }

    fun getSehriDua(): Dua? {
        return duas.find { it.type == "SEHRI" }
    }

    fun getIftarDua(): Dua? {
        return duas.find { it.type == "IFTAR" }
    }
}