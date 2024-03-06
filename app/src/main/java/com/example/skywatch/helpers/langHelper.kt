package com.example.skywatch.helpers

import androidx.appcompat.app.AppCompatDelegate


fun getLanguageLocale(): String {
    return AppCompatDelegate.getApplicationLocales().toLanguageTags()
}