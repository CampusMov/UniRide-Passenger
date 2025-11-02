package com.campusmov.uniride

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.campusmov.uniride.presentation.navigation.graph.root.RootNavGraph
import com.campusmov.uniride.ui.theme.UniRideTheme
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint

// CampusMov - UniRide
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()){
            Places.initialize(this, getString(R.string.google_maps_key))
        }
        installSplashScreen()
        setContent {
            UniRideTheme {
                Surface {
                    navHostController = rememberNavController()
                    RootNavGraph(navHostController = navHostController)
                }
            }
        }
    }
}
