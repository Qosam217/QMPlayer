package com.project.qmplayer

import android.app.Application
import com.project.qmplayer.di.AppModule
import com.project.qmplayer.di.QMPlayerContainer

class QMPlayerApplication: Application(){

    lateinit var container: QMPlayerContainer
        private set

    override fun onCreate() {
        super.onCreate()

        container = QMPlayerContainer(this)
    }
}