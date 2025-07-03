package com.campusmov.uniride.presentation.views.reputation.infractions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.domain.reputation.model.EPenaltyStatus
import com.campusmov.uniride.domain.reputation.model.Infraction

@Composable
fun IncidentsView(
    viewModel: IncidentsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val expanded = remember { mutableStateOf(false) }
    val penaltyStatus = remember { mutableStateOf("Active") }

    val infractionsList = viewModel.infractionsList.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color.Black)
        ) {
            IconButton(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .background(Color.Transparent),
                onClick = {
                    navHostController.popBackStack()
                },
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Icon of go back",
                    tint = Color.White
                )
            }

            Text(
                text = "Incidencias",
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp, horizontal = 16.dp),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(30.dp))

            Box {
                Button (onClick = { expanded.value = true }) {
                    Text(penaltyStatus.value.toString())
                }
                DropdownMenu (
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) {
                    EPenaltyStatus.entries.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status.name) },
                            onClick = {
                                penaltyStatus.value = status.name
                                expanded.value = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))



            LazyColumn {
                items(infractionsList.value.filter { it.status.name == penaltyStatus.value }) { infraction ->
                    IncidentItem(infraction = infraction)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }


        }
    }

}

@Composable
fun IncidentItem(
    infraction: Infraction,
){
    val expanded = remember { mutableStateOf(false) }

    Card (
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable { expanded.value = !expanded.value }
            ) {
                Text(
                    text = infraction.type,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    tint = Color.White
                )
            }

            if (expanded.value) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = infraction.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = infraction.formatDate(infraction.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}