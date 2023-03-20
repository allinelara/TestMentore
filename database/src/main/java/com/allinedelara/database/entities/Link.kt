package com.allinedelara.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Link(
    @PrimaryKey val id: String,
    val url: String,
    val author: String,
    val title: String,
    val ups: Int,
    val downs: Int,
)