package com.project.qmplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.project.qmplayer.core.ui.theme.QMPlayerTheme
import com.project.qmplayer.core.ui.components.HomeScreen

class MainActivity : ComponentActivity() {

    companion object{
        private const val TAG = "QMPlayer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as QMPlayerApplication).container
        setContent {
            QMPlayerTheme {
                HomeScreen(container)
            }
        }
    }
}