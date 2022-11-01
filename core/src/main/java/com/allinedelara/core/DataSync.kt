package com.allinedelara.core

interface DataSync <T,K>{

    suspend fun put(item: T)
    suspend fun putList(listItems: List<T>)
    suspend fun delete(id: K)

}