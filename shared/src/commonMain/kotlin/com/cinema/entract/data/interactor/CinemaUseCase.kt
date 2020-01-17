/*
 * Copyright 2019 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cinema.entract.data.interactor

import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.platform.DateUtils
import com.cinema.entract.data.platform.NetworkUtils
import com.cinema.entract.data.platform.PlatformUtils
import com.cinema.entract.data.source.DataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CinemaUseCase(
    private val dataStore: DataStore,
    private val networkUtils: NetworkUtils,
    private val platformUtils: PlatformUtils,
    private val dateUtils: DateUtils
) {

    private var currentDate: String? = null

    fun setDate(date: String) {
        currentDate = date
    }

    fun getDate(): String = currentDate ?: dateUtils.todayUtc().also { currentDate = it }

    suspend fun getMovies(): List<MovieData> = dataStore
        .getMovies(getDate())
        .map {
            if (!canDisplayMedia()) it.copy(
                coverUrl = "",
                teaserId = ""
            ) else it
        }

    fun getMovies(success: (List<MovieData>) -> Unit) {
        GlobalScope.launch(platformUtils.applicationDispatcher) {
            success(getMovies())
        }
    }

    suspend fun getMovie(movie: MovieData): MovieData {
        setDate(movie.date)
        return getMovies().first { it.movieId == movie.movieId }
    }

    suspend fun getDateRange(): DateRangeData? = dataStore.getDateRange()

    suspend fun getSchedule(): List<WeekData> = dataStore.getSchedule().filter { it.hasMovies }

    suspend fun getEventUrl(): String? =
        if (isPromotionalEnabled()) dataStore.getPromotionalUrl().takeIf { it.isNotEmpty() } else null

    private fun canDisplayMedia(): Boolean =
        !dataStore.getUserPreferences().isOnlyOnWifi() || networkUtils.isConnectedOnWifi()

    fun isPromotionalEnabled(): Boolean = dataStore.getUserPreferences().isPromotionalEnabled()

    fun setPromotionalPreference(enabled: Boolean) {
        dataStore.getUserPreferences().setPromotionalPreference(enabled)
    }

    fun isOnlyOnWifi(): Boolean = dataStore.getUserPreferences().isOnlyOnWifi()

    fun setOnlyOnWifi(onlyOnWifi: Boolean) {
        dataStore.getUserPreferences().setOnlyOnWifi(onlyOnWifi)
    }

    fun getPrefThemeMode(): String = dataStore.getUserPreferences().getPrefThemeMode()

    fun setPrefThemeMode(mode: String) {
        dataStore.getUserPreferences().setThemeMode(mode)
    }

    fun getThemeMode(): Int = dataStore.getUserPreferences().getThemeMode()
}
