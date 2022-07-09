package com.naipofo.utabrowser.data.remote.uta

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

class UtaExtractor(
    private val client: HttpClient,
) {
    suspend fun getPageData(url: String): ExtractedData {
        val doc = Jsoup.parse(client.get(url).bodyAsText())
        return ExtractedData(
            title =(doc.select(".newLyricTitle__main")[0].childNodes()[0] as TextNode).text().trim(),
            artist = doc.select(".newLyricWork__name")[0].text().trim(),
            lyrics = doc.select(".lyricBody div.hiragana")[0].childNodes().map {
                // <span class="ruby"><span class="rb">魚</span><span class="rt">さかな</span></span>な<br>
                when (it) {
                    is TextNode -> LyricNode.Text(it.text())
                    is Element -> {
                        if (it.`is`("br")) {
                            LyricNode.LineBreak
                        } else {
                            LyricNode.Ruby(
                                rb = it.select(".rb").text(),
                                rt = it.select(".rt").text()
                            )
                        }
                    }
                    else -> LyricNode.LineBreak
                }
            }
        )
    }
}