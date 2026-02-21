package com.shkhan.iqra.ui.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shkhan.iqra.ui.model.Quote
import kotlin.random.Random

class QuoteRepository(private val context: Context) {

    fun getRandomQuote(): String {

        val jsonString = context.assets
            .open("quotes.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Quote>>() {}.type
        val quotes: List<Quote> = Gson().fromJson(jsonString, type)

        return quotes[Random.nextInt(quotes.size)].text
    }
}