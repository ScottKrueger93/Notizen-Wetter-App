package com.example.abschlussprojektscott.data.model

data class WeatherData(
    var weather: List<Weather>,
    var coord: Coord,
    var name: String
) {
}