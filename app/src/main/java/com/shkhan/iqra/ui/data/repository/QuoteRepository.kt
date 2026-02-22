package com.shkhan.iqra.ui.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shkhan.iqra.ui.model.Quote
import java.io.InputStreamReader

class QuoteRepository(private val context: Context) {

    private var quotes: List<Quote> = emptyList()
    private var lastQuoteId: Int? = null

    init {
        loadQuotesFromJson()
    }

    private fun loadQuotesFromJson() {
        try {
            val inputStream = context.assets.open("quotes.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<Quote>>() {}.type
            quotes = Gson().fromJson(reader, type)
        } catch (e: Exception) {
            e.printStackTrace()
            quotes = emptyList()
        }
    }

    fun getRandomQuote(): Quote? {

        if (quotes.isEmpty()) return null

        val available = quotes.filter { it.id != lastQuoteId }

        val selected = if (available.isNotEmpty())
            available.random()
        else
            quotes.random()

        lastQuoteId = selected.id

        return selected
    }
}