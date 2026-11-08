package com.project.qmplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.project.qmplayer.core.ui.theme.QMPlayerTheme
import com.project.qmplayer.core.ui.components.HomeScreen

class MainActivity : ComponentActivity() {

    companion object{
        private const val TAG = "QMPlayer"
    }

    private val requestAudioPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // MediaStore observer will automatically reload if permission is granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestAudioPermissionIfNeeded()

        val container = (application as QMPlayerApplication).container
        setContent {
            QMPlayerTheme {
                HomeScreen(container)
            }
        }
    }

    private fun requestAudioPermissionIfNeeded() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            requestAudioPermissionLauncher.launch(permission)
        }
    }
}