package net.icxd.engine.util

import java.nio.file.Files
import java.nio.file.Paths

object File {
    @JvmStatic
    fun readFile(path: String): String {
        val str: String
        try {
            str = String(Files.readAllBytes(Paths.get(path)))
        } catch (e: Exception) {
            throw RuntimeException("Failed to load a file! $path")
        }
        return str
    }
}