package net.icxd.engine.util

import java.time.Instant

object Logger {
    fun log(level: LogLevel, message: String) {
        val now = Instant.now()
        val nowFormatted = now.toString().substring(0, 19).replace("T", " ")
        println("($nowFormatted) [$level] $message")
    }

    fun debug(message: String, vararg args: Any) { log(LogLevel.DEBUG, message.format(*args)) }
    fun info(message: String, vararg args: Any) { log(LogLevel.INFO, message.format(*args)) }
    fun warn(message: String, vararg args: Any) { log(LogLevel.WARN, message.format(*args)) }
    fun error(message: String, vararg args: Any) { log(LogLevel.ERROR, message.format(*args)) }
    fun fatal(message: String, vararg args: Any) { log(LogLevel.FATAL, message.format(*args)) }

    enum class LogLevel(private val color: ConsoleColor) {
        DEBUG(ConsoleColor.CYAN),
        INFO(ConsoleColor.GREEN),
        WARN(ConsoleColor.YELLOW),
        ERROR(ConsoleColor.RED),
        FATAL(ConsoleColor.PURPLE);

        override fun toString(): String {
            return color.toString() + name + ConsoleColor.RESET
        }
    }
}