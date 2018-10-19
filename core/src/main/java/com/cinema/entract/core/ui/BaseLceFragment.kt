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

package com.cinema.entract.core.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import com.cinema.entract.core.R
import com.cinema.entract.core.ext.find
import com.cinema.entract.core.ext.hide
import com.cinema.entract.core.ext.show
import com.cinema.entract.core.widget.ErrorView

@Suppress("UNCHECKED_CAST")
open class BaseLceFragment<T : View> : BaseFragment() {

    lateinit var contentView: T
    lateinit var loadingView: View
    lateinit var errorView: ErrorView

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contentView = find<View>(R.id.contentView) as T
        loadingView = find(R.id.loadingView)
        errorView = find(R.id.errorView)
    }

    @CallSuper
    open fun showContent() {
        errorView.hide()
        loadingView.hide()
        contentView.show()
    }

    @CallSuper
    open fun showLoading() {
        contentView.hide()
        errorView.hide()
        loadingView.show()
    }

    @CallSuper
    open fun showError(throwable: Throwable?, action: () -> Unit) {
        contentView.hide()
        loadingView.hide()
        errorView.show(getErrorDrawable(throwable), getErrorMessage(throwable), action)
    }
}