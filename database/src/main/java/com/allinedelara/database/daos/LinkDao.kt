package com.allinedelara.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.allinedelara.database.entities.Link

@Dao
interface LinkDao {

    @Query("select * from Link where id =:id")
    fun getLink(id:String) : Link

    @Insert
    fun insertLink(link: Link)
    
}