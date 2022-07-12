package com.naipofo.utabrowser.data

import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object DeflateUtils {
    fun deflate(string: String): ByteArray = ByteArrayOutputStream().apply {
        GZIPOutputStream(this).bufferedWriter().use { it.write(string) }
    }.toByteArray()

    fun inflate(bytes: ByteArray): String =
        GZIPInputStream(bytes.inputStream()).bufferedReader().use { it.readText() }
}