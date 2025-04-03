package com.yusuf.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yusuf.domain.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val context: Context
) : LocationRepository{

    private val fusedLocationProviderClient : FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    override suspend fun getLocation(): Location? {

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("LocationRepository", "Location permission not granted")
            return null
        }

        return fusedLocationProviderClient.lastLocation.await()
    }

    override suspend fun getLocationName(latitude: Double, longitude: Double): String {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    suspendCoroutine { cont ->
                        geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                            cont.resume(addresses.firstOrNull()?.locality ?: "Unknown city")
                        }
                    }
                } else {
                    suspendCoroutine { cont ->
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                        cont.resume(addresses?.firstOrNull()?.locality ?: "Unknown city")
                    }
                }
            } catch (e: Exception) {
                Log.e("LocationRepository", "Error fetching location name", e)
                "Error fetching location name $e"
            }
        }
    }

}