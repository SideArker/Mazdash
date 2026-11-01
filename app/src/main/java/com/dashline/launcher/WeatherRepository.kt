// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/** One upcoming day in the forecast. dateIso is "yyyy-MM-dd" (formatted by the UI). */
data class DailyForecast(val dateIso: String, val iconRes: Int, val high: String, val low: String)

/** Current weather plus a short multi-day forecast. */
data class Weather(
    val iconRes: Int,
    val description: String,
    val temp: String,
    val city: String = "",
    val forecast: List<DailyForecast>
)

/**
 * Fetches current weather from Open-Meteo (https://open-meteo.com) — a free API
 * that needs no key.
 *
 * Uses a robust hybrid location provider:
 * 1. Checks hardware GPS / system location
 * 2. If unavailable (e.g. car in garage or GPS fix pending), falls back to instant IP geolocation
 * 3. Falls back to previously cached coordinates in Prefs so weather is never blank
 */
object WeatherRepository {

    private const val TAG = "WeatherRepository"
    private val mainHandler = Handler(Looper.getMainLooper())

    private data class LocationPoint(val lat: Double, val lon: Double, val city: String)

    /** Loads weather on a background thread; delivers result (or null) on the UI thread. */
    fun load(context: Context, useFahrenheit: Boolean, callback: (Weather?) -> Unit) {
        Thread {
            val loc = resolveLocation(context)
            if (loc == null) {
                mainHandler.post { callback(null) }
                return@Thread
            }

            val unit = if (useFahrenheit) "fahrenheit" else "celsius"
            val url = "https://api.open-meteo.com/v1/forecast" +
                "?latitude=${loc.lat}&longitude=${loc.lon}" +
                "&current=temperature_2m,weather_code" +
                "&daily=weather_code,temperature_2m_max,temperature_2m_min" +
                "&forecast_days=4&temperature_unit=$unit&timezone=auto"

            val weather = try {
                val json = (URL(url).openConnection() as HttpURLConnection).run {
                    connectTimeout = 6000
                    readTimeout = 6000
                    inputStream.bufferedReader().use { it.readText() }
                }
                parse(context, json, loc.city)
            } catch (e: Exception) {
                Log.w(TAG, "Failed to fetch weather from Open-Meteo: ${e.message}")
                null
            }
            mainHandler.post { callback(weather) }
        }.start()
    }

    private fun resolveLocation(context: Context): LocationPoint? {
        val prefs = Prefs(context)

        // 1. Try hardware / platform GPS
        val gps = lastKnownLocation(context)
        if (gps != null && (gps.latitude != 0.0 || gps.longitude != 0.0)) {
            prefs.cachedLat = gps.latitude
            prefs.cachedLon = gps.longitude
            Log.d(TAG, "Using GPS location: ${gps.latitude}, ${gps.longitude}")
            return LocationPoint(gps.latitude, gps.longitude, prefs.cachedCity)
        }

        // 2. Try fast IP Geolocation (ideal when connected via Wi-Fi / phone hotspot)
        val ipLoc = fetchIpLocation()
        if (ipLoc != null) {
            prefs.cachedLat = ipLoc.lat
            prefs.cachedLon = ipLoc.lon
            prefs.cachedCity = ipLoc.city
            Log.d(TAG, "Using IP Geolocation: ${ipLoc.city} (${ipLoc.lat}, ${ipLoc.lon})")
            return ipLoc
        }

        // 3. Fallback to cached coordinates
        if (prefs.cachedLat != 0.0 || prefs.cachedLon != 0.0) {
            Log.d(TAG, "Using cached location: ${prefs.cachedCity} (${prefs.cachedLat}, ${prefs.cachedLon})")
            return LocationPoint(prefs.cachedLat, prefs.cachedLon, prefs.cachedCity)
        }

        // 4. Default fallback: Warsaw, Poland (default Mazda position in PL)
        return LocationPoint(52.23, 21.01, "Warszawa")
    }

    private fun fetchIpLocation(): LocationPoint? {
        return try {
            val conn = (URL("http://ip-api.com/json").openConnection() as HttpURLConnection).apply {
                connectTimeout = 3000
                readTimeout = 3000
            }
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            val root = JSONObject(text)
            if (root.optString("status") == "success") {
                LocationPoint(
                    lat = root.optDouble("lat", 0.0),
                    lon = root.optDouble("lon", 0.0),
                    city = root.optString("city", "")
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun parse(context: Context, json: String, city: String): Weather? {
        val root = JSONObject(json)
        val current = root.optJSONObject("current") ?: return null
        val temp = current.optDouble("temperature_2m", Double.NaN)
        if (temp.isNaN()) return null
        val code = current.optInt("weather_code", -1)
        val (iconRes, descRes) = describe(code)
        val rawDesc = context.getString(descRes)
        val fullDesc = if (city.isNotEmpty()) "$city • $rawDesc" else rawDesc

        return Weather(
            iconRes = iconRes,
            description = fullDesc,
            temp = "${Math.round(temp)}°",
            city = city,
            forecast = parseDaily(root.optJSONObject("daily"))
        )
    }

    /** Upcoming days only (skip index 0 = today). Up to 3 days. */
    private fun parseDaily(daily: JSONObject?): List<DailyForecast> {
        daily ?: return emptyList()
        val times = daily.optJSONArray("time") ?: return emptyList()
        val codes = daily.optJSONArray("weather_code") ?: return emptyList()
        val highs = daily.optJSONArray("temperature_2m_max") ?: return emptyList()
        val lows = daily.optJSONArray("temperature_2m_min") ?: return emptyList()
        val result = mutableListOf<DailyForecast>()
        var i = 1
        while (i < times.length() && result.size < 3) {
            val (icon, _) = describe(codes.optInt(i, -1))
            result.add(
                DailyForecast(
                    dateIso = times.optString(i),
                    iconRes = icon,
                    high = "${Math.round(highs.optDouble(i))}°",
                    low = "${Math.round(lows.optDouble(i))}°"
                )
            )
            i++
        }
        return result
    }

    /** Map a WMO weather code to an icon + description. */
    private fun describe(code: Int): Pair<Int, Int> = when (code) {
        0 -> R.drawable.ic_wx_clear to R.string.wx_clear
        1 -> R.drawable.ic_wx_clear to R.string.wx_mainly_clear
        2 -> R.drawable.ic_wx_partly to R.string.wx_partly_cloudy
        3 -> R.drawable.ic_wx_cloudy to R.string.wx_overcast
        45, 48 -> R.drawable.ic_wx_fog to R.string.wx_fog
        51, 53, 55, 56, 57 -> R.drawable.ic_wx_rain to R.string.wx_drizzle
        61, 63, 65, 66, 67 -> R.drawable.ic_wx_rain to R.string.wx_rain
        80, 81, 82 -> R.drawable.ic_wx_rain to R.string.wx_showers
        71, 73, 75, 77, 85, 86 -> R.drawable.ic_wx_snow to R.string.wx_snow
        95, 96, 99 -> R.drawable.ic_wx_thunder to R.string.wx_thunderstorm
        else -> R.drawable.ic_wx_cloudy to R.string.wx_overcast
    }

    private fun lastKnownLocation(context: Context): Location? {
        if (!hasLocationPermission(context)) return null
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            ?: return null
        return try {
            listOf(
                LocationManager.GPS_PROVIDER,
                LocationManager.NETWORK_PROVIDER,
                LocationManager.PASSIVE_PROVIDER
            ).asSequence()
                .mapNotNull { p -> runCatching { lm.getLastKnownLocation(p) }.getOrNull() }
                .maxByOrNull { it.time }
        } catch (e: SecurityException) {
            null
        }
    }

    private fun hasLocationPermission(context: Context): Boolean {
        val fine = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED ||
            coarse == PackageManager.PERMISSION_GRANTED
    }
}
