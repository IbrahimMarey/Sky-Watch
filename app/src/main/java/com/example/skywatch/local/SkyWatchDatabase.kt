package com.example.skywatch.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skywatch.Constants
import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.FavoritePojo

@Database(entities = [FavoritePojo::class, AlertPojo::class], version = 1, exportSchema = false)
abstract class SkyWatchDatabase :RoomDatabase()
{
    abstract fun SkyWatchDao():SkyWatchDao
    companion object{
        private var dbInstance:SkyWatchDatabase?=null
        fun getInstance(context: Context):SkyWatchDatabase
        {
            return dbInstance?: synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SkyWatchDatabase::class.java,
                    Constants.DatabaseName
                ).build()
                dbInstance =instance
                instance
            }
        }
    }
}