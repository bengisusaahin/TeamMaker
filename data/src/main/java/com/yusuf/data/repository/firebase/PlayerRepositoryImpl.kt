package com.yusuf.data.repository.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.data.mapper.toCompetitionData
import com.yusuf.data.mapper.toCompetitionDataDto
import com.yusuf.data.mapper.toPlayerData
import com.yusuf.data.mapper.toPlayerDataDto
import com.yusuf.data.remote.dto.firebase_dto.CompetitionDataDto
import com.yusuf.data.remote.dto.firebase_dto.PlayerDataDto
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.repository.firebase.player.PlayerRepository
import com.yusuf.domain.util.RootResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): PlayerRepository{

    override suspend fun getAllPlayers(): Flow<RootResult<List<PlayerData>>> = flow {
        emit(RootResult.Loading)
        try {
            val userIdResult = getCurrentUserId().first()
            if (userIdResult is RootResult.Success) {
                val userId = userIdResult.data
                if (userId != null) {
                    val querySnapshot = firestore.collection("users").document(userId).collection("players").get().await()
                    val playerList = querySnapshot.documents.mapNotNull { document ->
                        val playerDataDto = document.toObject(PlayerDataDto::class.java)
                        playerDataDto?.toPlayerData()
                    }
                    emit(RootResult.Success(playerList))
                } else {
                    emit(RootResult.Error("User ID is null"))
                }
            } else {
                emit(RootResult.Error("Failed to get user ID"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)


    override fun getCurrentUserId(): Flow<RootResult<String?>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            emit(RootResult.Success(userId))
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun addPlayer(playerData: PlayerData): Flow<RootResult<Boolean>> = flow {

        val playerInfo = playerData.toPlayerDataDto()

        emit(RootResult.Loading)
        try {
            val userIdResult = getCurrentUserId().first()
            if (userIdResult is RootResult.Success) {
                val userId = userIdResult.data
                if (userId != null) {
                    firestore.collection("users").document(userId).collection("players").add(playerInfo).await()
                    emit(RootResult.Success(true))
                } else {
                    emit(RootResult.Error("User ID is null"))
                }
            } else {
                emit(RootResult.Error("Failed to get user ID"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addCompetition(competitionData: CompetitionData): Flow<RootResult<Boolean>> = flow {
        val competitionInfo = competitionData.toCompetitionDataDto()

        emit(RootResult.Loading)
        try {
            val userIdResult = getCurrentUserId().first()
            if (userIdResult is RootResult.Success) {
                val userId = userIdResult.data
                if (userId != null) {
                    firestore.collection("users").document(userId).collection("competitions").add(competitionInfo).await()
                    emit(RootResult.Success(true))
                } else {
                    emit(RootResult.Error("User ID is null"))
                }
            } else {
                emit(RootResult.Error("Failed to get user ID"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllCompetitions(): Flow<RootResult<List<CompetitionData>>> = flow {
        emit(RootResult.Loading)
        try {
            val userIdResult = getCurrentUserId().first()
            if (userIdResult is RootResult.Success) {
                val userId = userIdResult.data
                if (userId != null) {
                    val querySnapshot = firestore.collection("users").document(userId).collection("competitions").get().await()
                    val competitionList = querySnapshot.documents.mapNotNull { document ->
                        val competitionDataDto = document.toObject(CompetitionDataDto::class.java)
                        competitionDataDto?.toCompetitionData()
                    }
                    emit(RootResult.Success(competitionList))
                } else {
                    emit(RootResult.Error("User ID is null"))
                }
            } else {
                emit(RootResult.Error("Failed to get user ID"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)
}