package com.campusmov.uniride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.campusmov.uniride.presentation.navigation.graph.root.RootNavGraph
import com.campusmov.uniride.ui.theme.UniRideTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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