package com.campusmov.uniride.presentation.views.routingmatching.menunavigation

import androidx.compose.foundation.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.campusmov.uniride.R
import com.campusmov.uniride.presentation.navigation.screen.analytic.AnalyticsScreen
import com.campusmov.uniride.presentation.navigation.screen.auth.AuthScreen
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen
import com.campusmov.uniride.presentation.navigation.screen.reputation.ReputationScreen
import com.campusmov.uniride.presentation.util.NavigationItem
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuNavigationView(
    viewModel: MenuNavigationViewModel = hiltViewModel(),
    navHostController: NavHostController,
    content: @Composable () -> Unit){
    val items = listOf(
        NavigationItem(
            title = "Mis Carpools",
            route = ProfileScreen.ProfileInfo.route,
            selectedIcon = Icons.Filled.Map,
            unselectedIcon = Icons.Outlined.Map
        ) {},
        NavigationItem(
            title = "Mi Tiempo",
            route = ProfileScreen.ProfileInfo.route,
            selectedIcon = Icons.Filled.Timelapse,
            unselectedIcon = Icons.Outlined.Timelapse
        ) {},
        NavigationItem(
            title = "Incidencias",
            route = ReputationScreen.infractions.route,
            selectedIcon = Icons.Filled.Warning,
            unselectedIcon = Icons.Outlined.Warning
        ) {},
        NavigationItem(
            title = "Ubicaciones Guardadas",
            route = ProfileScreen.ProfileInfo.route,
            selectedIcon = Icons.Filled.LocationOn,
            unselectedIcon = Icons.Outlined.LocationOn
        ) {},
        NavigationItem(
            title = "Notificaciones",
            route = ProfileScreen.ProfileInfo.route,
            selectedIcon = Icons.Filled.Notifications,
            unselectedIcon = Icons.Outlined.Notifications
        ) {},
        NavigationItem(
            title = "Cerrar sesión",
            route = AuthScreen.Welcome.route,
            selectedIcon = Icons.Filled.Logout,
            unselectedIcon = Icons.Outlined.Logout,
            function = {
                viewModel.logout()
            }
        )
    )

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = screenWidth * 0.75f

    var selectedItemIndex = rememberSaveable {
        mutableIntStateOf(0)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val rating = viewModel.rating.collectAsState()
    val profile = viewModel.profile.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(drawerWidth)
                    .background(Color.Black)
                    .padding(16.dp),
                drawerContainerColor = Color.Black
            ) {

                IconButton(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .background(Color.Transparent),
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Icon of go back", tint = Color.White)
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = profile.value?.firstName.orEmpty(),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = profile.value?.lastName.orEmpty(),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF424242))
                                .clickable {
                                    navHostController.navigate(route = AnalyticsScreen.StudentRatingMetrics.route)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.StarRate,
                                    contentDescription = "Rating",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = rating.value.toString().take(3),
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = { navHostController.navigate(route = ProfileScreen.ProfileInfo.route) },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                        ) {
                            Text(text = "Editar mi perfil", color = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Editar perfil",
                                tint = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.BottomEnd
                    ){
                        if (profile.value?.profilePictureUrl?.isNotBlank() == true) {
                            AsyncImage(
                                model = profile.value?.profilePictureUrl,
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.user_profile_icon),
                                contentDescription = "Default profile icon",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = item.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        },
                        selected = index == selectedItemIndex.intValue,
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            selectedContainerColor = Color.Transparent,
                        ),
                        onClick = {
                            navHostController.navigate(route = item.route)
                            selectedItemIndex.intValue = index
                            scope.launch { drawerState.close() }
                            item.function.invoke()
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex.intValue) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = Color.White
                            )
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.White
                    )
                }


            }
        }
    ) {
        LaunchedEffect(Unit) {
            viewModel.getProfileById()
        }

        Box() {
            content()
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 60.dp, start = 20.dp),
                onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF262626))
                ) {
                    Icon(
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }
        }
    }
}