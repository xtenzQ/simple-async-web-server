package com.nikitar.exception

open class MethodNotAllowedException(private val method: String) : Exception("Method is not allowed: $method") {
    fun getMethod(): String = method
}

open class FileNotFoundException(private val fileName: String) : Exception("File is not found: $fileName") {
    fun getFile(): String = fileName
}

open class VersionNotSupportedException(private val version: String) : Exception("Version is not supported: $version") {
    fun getVersion(): String = version
}