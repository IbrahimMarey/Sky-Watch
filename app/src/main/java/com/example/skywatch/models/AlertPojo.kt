package com.example.skywatch.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "AlertSkyWatch")
data class AlertPojo(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    val lat: Double,
    val lon: Double,
    val start: Long,
    val end: Long,
    val kind: String,
)

object AlertKind {
    const val NOTIFICATION = "NOTIFICATION"
    const val ALARM = "ALARM"
}