/*
 * Copyright 2018 Stéphane Baiget
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

package com.cinema.entract.app.ui.settings

import androidx.lifecycle.ViewModel
import com.cinema.entract.data.interactor.CinemaUseCase

class SettingsViewModel(private val useCase: CinemaUseCase) : ViewModel() {

    fun isEventEnabled(): Boolean = useCase.isEventEnabled()

    fun setEventPreference(enabled: Boolean) = useCase.setEventPreference(enabled)

    fun isOnlyOnWifi(): Boolean = useCase.isOnlyOnWifi()

    fun setOnlyOnWifi(onlyOnWifi: Boolean) = useCase.setOnlyOnWifi(onlyOnWifi)

    fun isDarkMode(): Boolean = useCase.isDarkMode()

    fun setDarkMode(dark: Boolean) = useCase.setDarkMode(dark)
}