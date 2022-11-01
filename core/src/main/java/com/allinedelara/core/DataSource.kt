package com.allinedelara.core

interface DataSource <T,K> {

    suspend fun get(id: K): T?
    suspend fun getAll():List<T>

}