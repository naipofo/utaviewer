package com.naipofo.utabrowser.di

import android.content.Context
import com.naipofo.utabrowser.BuildConfig
import com.naipofo.utabrowser.Database
import com.naipofo.utabrowser.data.local.favorites.FavoritesRepository
import com.naipofo.utabrowser.data.local.settings.SettingsRepository
import com.naipofo.utabrowser.data.remote.uta.UtaApi
import com.naipofo.utabrowser.data.remote.uta.UtaExtractor
import com.naipofo.utabrowser.data.remote.uta.UtaRepository
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val mainModule = DI.Module("main") {
    bindSingleton {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    bindSingleton {
        Database(
            AndroidSqliteDriver(Database.Schema, instance(), "base.db")
        )
    }
    bindSingleton {
        instance<Context>().getSharedPreferences("utabrowser", Context.MODE_PRIVATE)
    }

    bindSingleton("UtaKey") { BuildConfig.utakey }
    bindSingleton("UtaDomain") { BuildConfig.utadomain }

    bindSingleton { UtaExtractor(instance()) }
    bindSingleton { UtaApi(instance(), instance("UtaKey"), instance("UtaDomain")) }
    bindSingleton { UtaRepository(instance(), instance(), instance()) }

    bindSingleton { FavoritesRepository(instance(), instance()) }

    bindSingleton { SettingsRepository(instance()) }
}