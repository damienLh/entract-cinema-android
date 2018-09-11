/*
 * Copyright 2018 Stéphane Baiget
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cinema.entract.data.di

import com.cinema.entract.data.model.DayDataMapper
import com.cinema.entract.data.model.MovieDataMapper
import com.cinema.entract.data.model.WeekDataMapper
import com.cinema.entract.data.repository.CinemaRepositoryImpl
import com.cinema.entract.data.source.CinemaCacheDataStore
import com.cinema.entract.data.source.CinemaDataStoreFactory
import com.cinema.entract.data.source.CinemaRemoteDataStore
import com.cinema.entract.domain.repository.CinemaRepository
import org.koin.dsl.module.module

val dataModule = module {

    single { MovieDataMapper() }
    single { DayDataMapper(get()) }
    single { WeekDataMapper(get()) }

    factory { CinemaRemoteDataStore(get()) }
    factory { CinemaCacheDataStore(get()) }
    factory { CinemaDataStoreFactory(get(), get()) }

    factory { CinemaRepositoryImpl(get(), get(), get()) as CinemaRepository }
}