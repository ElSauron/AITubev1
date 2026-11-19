package com.elsauron.aitube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.elsauron.aitube.data.SecureKeyStore
import com.elsauron.aitube.ui.AITubeApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val keyStore = SecureKeyStore(applicationContext)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF6750A4),
                    secondary = Color(0xFFE5A000),
                    background = Color(0xFFF8F7FB),
                    surface = Color.White
                )
            ) {
                AITubeApp(keyStore)
            }
        }
    }
}
