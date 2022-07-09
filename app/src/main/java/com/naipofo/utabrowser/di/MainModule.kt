package com.naipofo.utabrowser.di

import com.naipofo.utabrowser.BuildConfig
import com.naipofo.utabrowser.data.remote.uta.UtaApi
import com.naipofo.utabrowser.data.remote.uta.UtaExtractor
import com.naipofo.utabrowser.data.remote.uta.UtaRepository
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val mainModule = DI {
    bindSingleton {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    bindSingleton("UtaKey") { BuildConfig.utakey }
    bindSingleton("UtaDomain") { BuildConfig.utadomain }

    bindSingleton { UtaExtractor(instance()) }
    bindSingleton { UtaApi(instance(), instance("UtaKey"), instance("UtaDomain")) }

    bindSingleton { UtaRepository(instance(), instance()) }
}