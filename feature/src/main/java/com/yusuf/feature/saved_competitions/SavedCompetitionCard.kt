package com.yusuf.feature.saved_competitions

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yusuf.component.ConfirmationDialog
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.navigation.NavigationGraph

@Composable
fun CompetitionCard(
    competition: SavedCompetitionsModel,
    onDeleteClick: (SavedCompetitionsModel) -> Unit,
    navController: NavController,
    padding: Dp = 8.dp,
) {
    var shouldShowItemDeletionDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth()
            .clickable {
                val route = NavigationGraph.getSavedCompetitionDetailsRoute(
                    SavedCompetitionsModel(
                        competitionId = competition.competitionId,
                        firstTeam = competition.firstTeam,
                        secondTeam = competition.secondTeam,
                        imageUrl = competition.imageUrl,
                        competitionTime = competition.competitionTime,
                        competitionDate = competition.competitionDate,
                        locationName = competition.locationName,
                        weatherModel = competition.weatherModel
                    )
                )
                navController.navigate(route)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(padding),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .clip(CircleShape),

                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                AsyncImage(
                    model = competition.imageUrl,
                    contentDescription = "Competition Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = competition.competitionTime,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = competition.competitionDate,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    tint = Color.Black,
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            shouldShowItemDeletionDialog = true
                        }
                )
                if (shouldShowItemDeletionDialog) {
                    ConfirmationDialog({
                        shouldShowItemDeletionDialog = it
                    }, {
                        onDeleteClick(competition)
                    })
                }
            }
        }
    }
}