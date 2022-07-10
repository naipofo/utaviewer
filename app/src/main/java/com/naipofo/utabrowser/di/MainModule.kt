package com.naipofo.utabrowser.di

import android.app.Activity
import com.naipofo.utabrowser.BuildConfig
import com.naipofo.utabrowser.Database
import com.naipofo.utabrowser.data.remote.uta.UtaApi
import com.naipofo.utabrowser.data.remote.uta.UtaExtractor
import com.naipofo.utabrowser.data.remote.uta.UtaRepository
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindings.WeakContextScope

val mainModule = DI.Module("main") {
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

    bindSingleton { UtaRepository(instance(), instance(), instance()) }

    bindSingleton {
        Database(
            AndroidSqliteDriver(Database.Schema, instance(), "base.db")
        )
    }
}