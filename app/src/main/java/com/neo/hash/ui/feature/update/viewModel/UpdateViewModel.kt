package com.neo.hash.ui.feature.update.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.neo.hash.BuildConfig
import com.neo.hash.domain.model.UpdateVersion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UpdateViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UpdateUiState())
    val uiState = _uiState.asStateFlow()

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    init {
        initRemoteConfig()
        getConfig()
    }

    private fun getConfig() {
        remoteConfig
            .fetchAndActivate()
            .addOnSuccessListener {
                _uiState.update {
                    it.copy(
                        update = UpdateVersion(
                            lastVersion = remoteConfig.getLong("version").toInt(),
                            lastRequiredVersion = remoteConfig.getLong("required_version").toInt(),
                            lastVersionName = remoteConfig.getString("version_name")
                        )
                    )
                }
            }.addOnFailureListener {
                Firebase.crashlytics.recordException(it)
            }
    }

    private fun initRemoteConfig() {
        if (BuildConfig.DEBUG) {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
            remoteConfig.setConfigSettingsAsync(configSettings)
        }

        remoteConfig.setDefaultsAsync(
            mapOf(
                "version" to BuildConfig.VERSION_CODE,
                "required_version" to BuildConfig.VERSION_CODE,
                "version_name" to BuildConfig.VERSION_NAME
            )
        )
    }
}