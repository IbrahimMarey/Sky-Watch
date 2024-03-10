package com.example.skywatch.helpers

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat


fun getLanguageLocale(): String {
    return AppCompatDelegate.getApplicationLocales().toLanguageTags()
}

fun changeLanguageLocaleTo(lan: String) {
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lan)
    AppCompatDelegate.setApplicationLocales(appLocale)
}