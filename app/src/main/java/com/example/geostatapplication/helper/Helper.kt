package com.example.geostatapplication.helper

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.util.Locale

fun limitWords(input: String, limit: Int): String {
    val words = input.split("\\s+".toRegex())
    if (words.size <= limit) {
        return input
    }
    val limitedWords = words.subList(0, limit)
    val limitedString = limitedWords.joinToString(separator = " ")

    return "$limitedString ..."
}

fun capitalizeWords(text: String): String {
    return text.split(" ").joinToString(" ") { word ->
        word.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
    }
}

