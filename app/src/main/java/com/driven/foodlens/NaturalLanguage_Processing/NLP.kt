package com.driven.foodrecipeapp.NaturalLanguage_Processing

import android.content.Context
import android.util.Log
import com.google.mlkit.nl.languageid.LanguageIdentification

class NLP(private val context: Context) {

    fun processNLPQuery(query: String, onProcessed: (String) -> Unit) {
        val languageIdentifier = LanguageIdentification.getClient()

        languageIdentifier.identifyLanguage(query)
            .addOnSuccessListener { languageCode ->
                if (languageCode != "und") {
                    Log.d("NLP", "Language: $languageCode")
                    onProcessed(query)
                } else {
                    Log.d("NLP", "Can't identify language.")
                    onProcessed(query)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("NLP", "Failed to identify language.", exception)
                onProcessed(query)
            }
    }
}