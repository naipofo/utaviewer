package com.naipofo.utabrowser.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.naipofo.utabrowser.data.local.settings.SettingsRepository
import com.naipofo.utabrowser.data.model.LyricNode
import com.naipofo.utabrowser.ui.screens.song.rubyRender
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SettingsRoute() {
    val settingsRepository: SettingsRepository by localDI().instance()

    var sizeMultiplier by remember { mutableStateOf(settingsRepository.sizeMultiplier) }
    var showRuby by remember { mutableStateOf(settingsRepository.showRuby) }

    Column {
        Text(text = "Lyrics size", style = MaterialTheme.typography.displayMedium)
        LazyColumn(
            Modifier
                .height(150.dp)
                .fillMaxWidth()
        ) {
            rubyRender(LyricNode.sample, sizeMultiplier, settingsRepository.showRuby)
            rubyRender(LyricNode.sample, sizeMultiplier, settingsRepository.showRuby)
            rubyRender(LyricNode.sample, sizeMultiplier, settingsRepository.showRuby)
            rubyRender(LyricNode.sample, sizeMultiplier, settingsRepository.showRuby)
        }
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = sizeMultiplier,
            valueRange = 0.3f..3f,
            onValueChange = {
                sizeMultiplier = it
            },
            onValueChangeFinished = {
                settingsRepository.sizeMultiplier = sizeMultiplier
            }
        )
        Row {
            Text(
                text = "Show Furigana",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.weight(1f)
            )
            Switch(checked = showRuby, onCheckedChange = {
                showRuby = it
                settingsRepository.showRuby = showRuby
            })
        }
    }
}