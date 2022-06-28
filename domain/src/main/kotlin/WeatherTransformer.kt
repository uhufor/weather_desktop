import arrow.core.Either
import arrow.core.continuations.either

class WeatherTransformer {

    private fun extractCurrentWeatherFrom(response: WeatherResponse): Either<Throwable, WeatherCard> =
        Either.catch {
            WeatherCard(
                condition = response.current.condition.text,
                iconUrl = "https:" + response.current.condition.icon.replace("64x64", "128x128"),
                temperature = response.current.tempC,
                feelsLike = response.current.feelslikeC,
            )
        }

    // 1
    private fun extractForecastWeatherFrom(response: WeatherResponse): Either<Throwable, List<WeatherCard>> =
        Either.catch {
            response.forecast.forecastday.map { forecastDay ->
                WeatherCard(
                    condition = forecastDay.day.condition.text,
                    iconUrl = "https:" + forecastDay.day.condition.icon,
                    temperature = forecastDay.day.avgtempC,
                    feelsLike = avgFeelsLike(forecastDay),
                    chanceOfRain = avgChanceOfRain(forecastDay),
                )
            }
        }

    // 2
    private fun avgFeelsLike(forecastDay: Forecastday): Double =
        forecastDay.hour.map(Hour::feelslikeC).average()

    private fun avgChanceOfRain(forecastDay: Forecastday): Double =
        forecastDay.hour.map(Hour::chanceOfRain).average()

    fun transform(response: WeatherResponse): Either<Throwable, WeatherResults> =
        either.eager {
            WeatherResults(
                currentWeather = extractCurrentWeatherFrom(response).bind(),
                forecast = extractForecastWeatherFrom(response).bind(),
            )
        }
}
