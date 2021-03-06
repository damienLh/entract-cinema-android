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

import com.cinema.entract.data.BuildConfig
import com.cinema.entract.data.source.DataStore

class TagUseCase(private val dataStore: DataStore) {

    suspend fun tagSchedule() = performTag { dataStore.tagSchedule() }

    suspend fun tagEvent() = performTag { dataStore.tagEvent() }

    suspend fun tagDetails(sessionId: String) = performTag { dataStore.tagDetails(sessionId) }

    suspend fun tagCalendar(sessionId: String) = performTag { dataStore.tagCalendar(sessionId) }

    private inline fun performTag(tag: () -> Unit) {
        if (!BuildConfig.DEBUG) tag()
    }
}