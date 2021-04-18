import com.google.gson.annotations.SerializedName

data class WeatherModel(

    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("currently") val currently: Currently
)