package com.campusmov.uniride.presentation.views.routingmatching.menunavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CrisisAlert
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.campusmov.uniride.presentation.navigation.graph.routingmatching.RoutingMatchingNavGraph
import com.campusmov.uniride.presentation.navigation.screen.auth.AuthScreen
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen
import com.campusmov.uniride.presentation.util.NavigationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuNavigationView(
    navHostController: NavHostController = rememberNavController(),
    drawerState: DrawerState,
    scope: CoroutineScope
){
    val items = listOf(

        NavigationItem(
            title = "Mis Carpools",
            route = ProfileScreen.ProfileInfo.route,
            selectedIcon = Icons.Filled.Map,
            unselectedIcon = Icons.Outlined.Map
        ),
        NavigationItem(
            title = "Mi Billetera",
            route = ProfileScreen.ProfileInfo.route,
            selectedIcon = Icons.Filled.AttachMoney,
            unselectedIcon = Icons.Outlined.AttachMoney
        ),
        NavigationItem(
            title = "Incidencias",
            route = ProfileScreen.ProfileInfo.route,
            selectedIcon = Icons.Filled.CrisisAlert,
            unselectedIcon = Icons.Outlined.CrisisAlert
        ),
        NavigationItem(
            title = "Ubicacion guardadas",
            route = ProfileScreen.ProfileInfo.route,
            selectedIcon = Icons.Filled.LocationOn,
            unselectedIcon = Icons.Outlined.LocationOn
        ),
        NavigationItem(
            title = "Notificaciones",
            route = ProfileScreen.ProfileInfo.route,
            selectedIcon = Icons.Filled.Notifications,
            unselectedIcon = Icons.Outlined.Notifications
        )

    )

    var selectedItemIndex = rememberSaveable {
        mutableIntStateOf(0)
    }




    //Captura pantalla
    val screeWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = screeWidth * 0.75f

    ModalDrawerSheet(
        modifier = Modifier.width(drawerWidth)
    ) {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
        ) {
            // Aquí va todo el contenido que ya tienes (header, items, cerrar sesión, etc.)
            // Ejemplo:
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Elmer Riva Rodriguez",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    TextButton(onClick = {
                        navHostController.navigate(ProfileScreen.ProfileInfo.route)
                        selectedItemIndex.intValue = 0
                        scope.launch { drawerState.close() }
                    }) {
                        Text(text = "Editar Perfil >", color = Color.White)
                    }
                }
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            items.forEachIndexed { index, item ->
                NavigationDrawerItem(
                    label = { Text(text = item.title, color = Color.White) },
                    selected = index == selectedItemIndex.intValue,
                    onClick = {
                        navHostController.navigate(item.route)
                        selectedItemIndex.intValue = index
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedItemIndex.intValue) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }

            Box {
                TextButton(onClick = {
                    navHostController.navigate(AuthScreen.Welcome.route) {
                        popUpTo(navHostController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }) {
                    Text(text = "Cerrar Sesion", color = Color.Red)
                }
            }
        }
    }
}