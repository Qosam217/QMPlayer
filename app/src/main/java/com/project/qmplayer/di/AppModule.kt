package com.project.qmplayer.di

import android.content.Context
import com.project.qmplayer.QMPlayerApplication

object AppModule {

    lateinit var applicationContext: Context
        private set

    fun initialize(application: QMPlayerApplication) {
        applicationContext = application.applicationContext
    }
}