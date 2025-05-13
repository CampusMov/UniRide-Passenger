package com.campusmov.uniride.presentation.views.routingmatching.mapcarpoolsearcher

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun MapCarpoolSearcherView(navHostController: NavHostController){
    Scaffold { paddingValues ->
        Text(text = "Map searcher screen", modifier = Modifier.padding(paddingValues))
    }
}