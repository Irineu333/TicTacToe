package com.neo.hash.data

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val flow: Flow<String>
    suspend fun setReferenceCode(code: String)
    suspend fun clearReferenceCode()
}