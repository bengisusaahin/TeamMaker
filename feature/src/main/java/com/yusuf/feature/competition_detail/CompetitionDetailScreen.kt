package com.yusuf.feature.competition_detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yusuf.domain.model.competition_detail.CompetitionDetail
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.feature.competition_detail.viewmodel.CompetitionDetailViewModel
import com.yusuf.feature.create_competition.location.LocationScreen
import com.yusuf.feature.competition_detail.weather.Weather

@Composable
fun CompetitionDetailScreen(
    navController: NavController,
    competitionDetail: CompetitionDetail?=null,
    competitionDetailViewModel: CompetitionDetailViewModel = hiltViewModel()
) {

    val saveCompetitionState by competitionDetailViewModel.competitionDetailState.collectAsState()
    var sentWeatherName by remember { mutableStateOf("") }

    if (competitionDetail != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = competitionDetail.selectedDate,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Log.d("MatchDetailScreen", "CompetitionDetail: $competitionDetail")

            Spacer(modifier = Modifier.height(2.dp))

            Weather(
                location = competitionDetail.location!!,
                locationName = competitionDetail.locationName!!
            ){
                weather ->
                sentWeatherName = weather
            }

            Spacer(modifier = Modifier.height(16.dp))

            TeamListScreen(
                firstTeam = competitionDetail.firstBalancedTeam,
                secondTeam = competitionDetail.secondBalancedTeam
            )

            val savedCompetition = SavedCompetitionsModel(
                firstTeam = competitionDetail.firstBalancedTeam,
                secondTeam = competitionDetail.secondBalancedTeam,
                imageUrl = "",
                competitionTime = competitionDetail.selectedTime,
                competitionDate = competitionDetail.selectedDate,
                locationName = competitionDetail.locationName!!,
                weather = sentWeatherName
            )
            Button(onClick = {
                competitionDetailViewModel.saveCompetition(savedCompetition)
            }) {
                Text(text = "Save Competition")
            }
        }
    } else {
        Text(text = "No competition detail found")
    }
}






