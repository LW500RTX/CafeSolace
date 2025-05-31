import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

suspend fun fetchCurrentTemperature(lat: Double, lon: Double): Double? {
    return withContext(Dispatchers.IO) {
        try {
            val response = URL(
                "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current_weather=true"
            ).readText()

            val json = JSONObject(response)
            val currentWeather = json.getJSONObject("current_weather")
            currentWeather.getDouble("temperature")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
