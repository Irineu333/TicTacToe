package com.neo.hash.data

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val referenceCodeFlow: Flow<String>
    val interstitialSkippedFlow : Flow<Long>
    suspend fun setReferenceCode(code: String)
    suspend fun clearReferenceCode()
    suspend fun interstitialSkipped()
    suspend fun interstitialSkipReset()
}