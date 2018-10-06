package com.cinema.entract.data.interactor

import com.cinema.entract.core.utils.NetworkUtils
import com.cinema.entract.data.repository.CinemaRepository

class PreferencesUseCase(
    private val repo: CinemaRepository,
    private val networkUtils: NetworkUtils
) {

    fun isEventEnabled(): Boolean = repo.getUserPreferences().isEventEnabled()

    fun setEventPreference(enabled: Boolean) = repo.getUserPreferences().setEventPreference(enabled)

    fun canDisplayMedia(): Boolean =
        !repo.getUserPreferences().isOnlyOnWifi() || networkUtils.isConnectedOnWifi()

    fun isOnlyOnWifi(): Boolean = repo.getUserPreferences().isOnlyOnWifi()

    fun setOnlyOnWifi(onlyOnWifi: Boolean) = repo.getUserPreferences().setOnlyOnWifi(onlyOnWifi)
}