package com.example.skywatch.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "FavoriteLocations", primaryKeys = ["lat","lng"])
data class FavoritePojo (val address:String ,val lat: Double,val lng :Double)