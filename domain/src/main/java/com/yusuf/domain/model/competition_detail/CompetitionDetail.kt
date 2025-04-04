package com.yusuf.domain.model.competition_detail

import android.location.Location
import com.yusuf.domain.model.firebase.PlayerData
import java.io.Serializable

data class CompetitionDetail(
    val selectedTime: String,
    val selectedDate: String,
    val firstBalancedTeam: List<PlayerData>? = null,
    val secondBalancedTeam: List<PlayerData>? = null,
    val locationName: String? = null,
    val imageUrl: String? = null,
    val competitionName: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
) : Serializable
