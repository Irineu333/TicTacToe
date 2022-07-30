package com.neo.hash.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : DataStoreRepository {

    private val userDataStore = context.userDataStore

    override val flow: Flow<String> = userDataStore.data.map { preferences ->
        preferences[REFERENCE_CODE_KEY] ?: EMPTY_REFERENCE_CODE
    }

    override suspend fun setReferenceCode(code: String) {
        userDataStore.edit {
            it[REFERENCE_CODE_KEY] = code
        }
    }

    override suspend fun clearReferenceCode() {
        userDataStore.edit {
            it[REFERENCE_CODE_KEY] = EMPTY_REFERENCE_CODE
        }
    }

    companion object {
        private val Context.userDataStore by preferencesDataStore(name = "user_data")
        private val REFERENCE_CODE_KEY = stringPreferencesKey(name = "reference_code")
        private const val EMPTY_REFERENCE_CODE = ""
    }
}
