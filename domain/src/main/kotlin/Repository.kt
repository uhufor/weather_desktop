/***
Copyright (c) 2021 Razeware LLC

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or
sell copies of the Software, and to permit persons to whom
the Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

Notwithstanding the foregoing, you may not use, copy, modify,
merge, publish, distribute, sublicense, create a derivative work,
and/or sell copies of the Software in any work that is designed,
intended, or marketed for pedagogical or instructional purposes
related to programming, coding, application development, or
information technology. Permission for such use, copying,
modification, merger, publication, distribution, sublicensing,
creation of derivative works, or sale is expressly withheld.

This project and source code may use libraries or frameworks
that are released under various Open-Source licenses. Use of
those libraries and frameworks are governed by their own
individual licenses.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
DEALINGS IN THE SOFTWARE.
 ***/

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class Repository(
    private val apiKey: String,
    private val client: HttpClient = defaultHttpClient,
) {
    private val transformer: WeatherTransformer = WeatherTransformer()

    private suspend fun getWeatherForCity(city: String): Either<Throwable, WeatherResponse> {
        return try {
            val urlString = "https://api.weatherapi.com/v1/forecast.json" +
                    "?key=$apiKey" +
                    "&q=$city" +
                    "&days=5" +
                    "&aqi=no" +
                    "&alerts=no"
            val response: WeatherResponse = client.get(urlString).body()
            response.right()
        } catch (e: Throwable) {
            e.left()
        }
    }

    suspend fun weatherForCity(city: String): Either<Throwable, WeatherResults> =
        either {
            val result = getWeatherForCity(city).bind()
            val content = transformer.transform(result).bind()
            content
        }

    companion object {
        val defaultHttpClient = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
}
