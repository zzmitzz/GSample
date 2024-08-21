package com.example.apiretrofitktor.data.remote.ktor

import com.example.apiretrofitktor.ServiceLocatorAPI
import com.example.apiretrofitktor.data.model.Response
import com.example.apiretrofitktor.data.remote.NetworkService
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

class KtorClient : NetworkService {
    private val client =
        HttpClient(OkHttp) {
            defaultRequest {
                url(ServiceLocatorAPI.API_LINK)
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

    override suspend fun getPokemonList(
        limit: Int,
        offset: Int,
    ): Response {
        return client.request("/api/v2/pokemon?limit=$limit&offset=$offset"){
            method = HttpMethod.Get
        }.body()
    }
}
