package com.example.skywatch.helpers

import com.example.skywatch.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

fun getTimeFormat(timeInMilliSecond: Long): String {
    val date = Date(timeInMilliSecond)
    val convertFormat =
        SimpleDateFormat("hh:mm a", Locale.getDefault())
    return convertFormat.format(date).toString()
}

fun getDayFormat(timeInMilliSecond: Long): Int{
    val date = Date(timeInMilliSecond)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return when(calendar.get(Calendar.DAY_OF_WEEK)){
        1-> R.string.sunday
        2-> R.string.monday
        3-> R.string.tuesday
        4-> R.string.wednesday
        5-> R.string.thursday
        6-> R.string.friday
        else -> R.string.saturday
    }
}

fun getCurrentDateFormat(): String {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val simpleDateFormat = SimpleDateFormat(pattern, Locale(getLanguageLocale()))
    return simpleDateFormat.format(Date(System.currentTimeMillis()))
}



fun getTimeFormat(timeInMilliSecond: Long,timeZone : TimeZone): String {
    val calendar = GregorianCalendar(timeZone)
    calendar.timeInMillis = timeInMilliSecond
    val convertFormat =
        SimpleDateFormat("hh:mm a", Locale.getDefault())
    convertFormat.timeZone = timeZone
    return convertFormat.format(calendar.time).toString()
}

fun getDayFormat(timeInMilliSecond: Long,timeZone : TimeZone): Int{
    timeZone.id

    val calendar = GregorianCalendar(timeZone)
    calendar.timeInMillis = timeInMilliSecond
    return when(calendar.get(Calendar.DAY_OF_WEEK)){
        1-> R.string.sunday
        2-> R.string.monday
        3-> R.string.tuesday
        4-> R.string.wednesday
        5-> R.string.thursday
        6-> R.string.friday
        else -> R.string.saturday
    }
}

fun getACompleteDateFormat(timeInMilliSecond: Long,timeZone : TimeZone):String{
    val pattern = "dd - MMMM - yyyy"
    val simpleDateFormat = SimpleDateFormat(pattern, Locale(getLanguageLocale()))
    simpleDateFormat.timeZone = timeZone
    return simpleDateFormat.format(Date(timeInMilliSecond))
}
fun getADateFormat(timeInMilliSecond: Long):String{
    val pattern = "dd MMMM"
    val simpleDateFormat = SimpleDateFormat(pattern, Locale(getLanguageLocale()))
    return simpleDateFormat.format(Date(timeInMilliSecond))
}
