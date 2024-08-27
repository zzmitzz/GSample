package com.example.apiretrofitktor.data.remote.ktor

import com.example.apiretrofitktor.data.Constant
import com.example.apiretrofitktor.data.remote.model.PokemonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject


class KtorClient @Inject constructor()   {
    private val client =
        HttpClient(OkHttp) {
            defaultRequest {
                url(Constant.API_LINK)
            }

            install(Logging) {
                logger = Logger.SIMPLE
            }

            engine {
                addInterceptor { chain ->
                    val request =
                        chain
                            .request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build()
                    chain.proceed(request)
                }
            }
            install(ContentNegotiation){
                json(Json{
                    ignoreUnknownKeys = true
                })
            }
        }

    suspend fun getPokemonList(
        limit: Int,
        offset: Int,
    ): PokemonResponse {
        return client.request("/api/v2/pokemon?limit=$limit&offset=$offset"){
            method = HttpMethod.Get
        }.body()
    }
}
