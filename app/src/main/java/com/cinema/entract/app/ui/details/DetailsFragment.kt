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

package com.cinema.entract.app.ui.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.find
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.ext.observe
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.base.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailsFragment : BaseFragment() {

    private val detailsViewModel by viewModel<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(detailsViewModel.getMovie(), ::displayMovie)
    }

    private fun displayMovie(movie: Movie?) {
        movie?.let {
            find<ImageView>(R.id.cover).load(movie.coverUrl)
            find<TextView>(R.id.title).text = movie.title
            find<TextView>(R.id.director).text = movie.director
            movie.cast.apply {
                val view = find<TextView>(R.id.cast)
                if (isEmpty()) view.isVisible = false
                else view.text = this
            }
            find<TextView>(R.id.year).text = movie.yearOfProduction
            find<TextView>(R.id.schedule).text = movie.schedule
            find<TextView>(R.id.duration).text = movie.duration
            find<TextView>(R.id.genre).text = movie.genre

            val synopsis = find<TextView>(R.id.synopsis)
            synopsis.text = movie.synopsis
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                synopsis.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
            }

            val teaser = find<Button>(R.id.teaser)
            if (movie.teaserId.isNotEmpty()) {
                teaser.setOnClickListener { _ -> showTeaser(it) }
            } else {
                teaser.isVisible = false
            }
            find<FloatingActionButton>(R.id.fab).setOnClickListener { _ -> addCalendarEvent(it) }
        }
    }

    private fun showTeaser(movie: Movie) {
        val appIntent = Intent(Intent.ACTION_VIEW, "vnd.youtube:${movie.teaserId}".toUri())
        val webIntent =
            Intent(Intent.ACTION_VIEW, "http://www.youtube.com/watch?v=${movie.teaserId}".toUri())
        try {
            requireContext().startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            requireContext().startActivity(webIntent)
        }
    }

    private fun addCalendarEvent(movie: Movie) {
        val (beginTime, endTime) = detailsViewModel.getEventSchedule(movie)
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            .putExtra(Events.TITLE, movie.title)
            .putExtra(Events.DESCRIPTION, getString(R.string.app_name))
            .putExtra(Events.EVENT_LOCATION, getString(R.string.information_address))
            .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
        startActivity(intent)
    }

    companion object {
        fun newInstance() = DetailsFragment()
    }
}