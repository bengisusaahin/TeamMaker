package com.yusuf.feature.create_competition


import android.location.Location
import androidx.compose.ui.Modifier
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yusuf.feature.create_competition.date_time.DatePickerWithDialog
import com.yusuf.feature.create_competition.date_time.TimePicker
import com.yusuf.feature.create_competition.location.LocationScreen
import com.yusuf.feature.create_competition.select_player.SelectPlayerScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateCompetitionScreen(navController: NavController) {
    var selectedTime by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("")}
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var selectedLocationName by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.let { handle ->
            if (handle.contains("reset")) {
                selectedTime = ""
                selectedDate = ""
                handle.remove<Boolean>("reset")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimePicker(
                onTimeSelected = { time ->
                    selectedTime = time
                }
            )

            DatePickerWithDialog(
                onDateSelected = { date ->
                    selectedDate = date
                }
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            LocationScreen(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                location, locationName ->
                selectedLocation = location
                selectedLocationName = locationName
            }
        }

        SelectPlayerScreen(navController, timePicker = selectedTime, datePicker = selectedDate, location = selectedLocation, locationName = selectedLocationName)
    }
}

