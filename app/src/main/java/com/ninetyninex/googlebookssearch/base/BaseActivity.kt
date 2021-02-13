package com.ninetyninex.googlebookssearch.base

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.ninetyninex.googlebookssearch.ui.main.volume.BookVolumeDetailsActivity
import io.reactivex.disposables.CompositeDisposable
import java.net.ConnectException

open class BaseActivity : AppCompatActivity() {

    protected open val subscription = CompositeDisposable()

    /**
     * Set window full screen
     */
    fun fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    /**
     * progress bar
     * @param view:ProgressBar
     * @param show:Boolean
     */
    fun showProgress(view: ProgressBar, show: Boolean) {

        if (show)
            view.visibility = View.VISIBLE
        else
            view.visibility = View.INVISIBLE
    }

    /**
     * handle network error
     */
    fun handleNetworkError(e: Throwable) {
        e.printStackTrace()
        if (e is ConnectException) {

        } else {

        }
    }
}