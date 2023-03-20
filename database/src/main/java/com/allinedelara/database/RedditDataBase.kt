package com.allinedelara.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.allinedelara.database.daos.LinkDao
import com.allinedelara.database.entities.Link

@Database(entities = [Link::class], version = 1, exportSchema = true)
abstract class RedditDataBase: RoomDatabase() {

    abstract fun linkDao() : LinkDao

}