package com.neo.hash.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    context: Context
) : DataStoreRepository {

    private val userDataStore = context.userDataStore
    private val userDataStoreFlow = userDataStore.data

    override val interstitialSkippedFlow: Flow<Long> =
        userDataStoreFlow.map {
            it[INTERSTITIAL_SKIP_COUNT_KEY] ?: 0L
        }

    override val referenceCodeFlow: Flow<String> =
        userDataStoreFlow.map {
            it[REFERENCE_CODE_KEY] ?: ""
        }

    override suspend fun setReferenceCode(code: String) {
        userDataStore.edit {
            it[REFERENCE_CODE_KEY] = code
        }
    }

    override suspend fun clearReferenceCode() {
        userDataStore.edit {
            it[REFERENCE_CODE_KEY] = ""
        }
    }

    override suspend fun interstitialSkipped() {
        userDataStore.edit { preferences ->
            val oldValue = preferences[INTERSTITIAL_SKIP_COUNT_KEY] ?: 0L
            preferences[INTERSTITIAL_SKIP_COUNT_KEY] = oldValue + 1
        }
    }

    override suspend fun interstitialSkipReset() {
        userDataStore.edit {
            it[INTERSTITIAL_SKIP_COUNT_KEY] = 0L
        }
    }

    companion object {
        private val Context.userDataStore by preferencesDataStore(name = "user_data")

        private val REFERENCE_CODE_KEY =
            stringPreferencesKey(name = "reference_code")

        private val INTERSTITIAL_SKIP_COUNT_KEY =
            longPreferencesKey(name = "interstitial_skip_count")
    }
}
