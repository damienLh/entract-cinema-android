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

package com.cinema.entract.remote.di

import com.cinema.entract.data.repository.RemoteRepo
import com.cinema.entract.remote.CinemaRemoteRepo
import com.cinema.entract.remote.CinemaService
import com.cinema.entract.remote.mapper.*
import com.cinema.entract.remote.network.ConnectivityInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

val remoteModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(ConnectivityInterceptor(get()))
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    single<CinemaService> {
        Retrofit.Builder()
            .baseUrl("http://mobile-grenadecinema.fr/php/rest/")
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    single { MovieMapper() }
    single { DayMapper(get()) }
    single { WeekMapper(get()) }
    single { DateRangeMapper() }
    single { PromotionalMapper() }

    factory<RemoteRepo> { CinemaRemoteRepo(get(), get(), get(), get(), get()) }
}

private const val TIME_OUT = 30L
