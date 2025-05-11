package com.campusmov.uniride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.campusmov.uniride.presentation.views.WelcomeView
import com.campusmov.uniride.ui.theme.UniRideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            UniRideTheme {
                WelcomeView()
            }
        }
    }
}