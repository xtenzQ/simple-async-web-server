package com.nikitar.parser

import com.nikitar.exception.FileNotFoundException
import com.nikitar.exception.MethodNotAllowedException
import com.nikitar.exception.VersionNotSupportedException
import com.nikitar.model.HttpMethod
import com.nikitar.model.HttpRequest
import com.nikitar.model.HttpVersion
import java.io.File
import java.net.Socket
import java.util.logging.Logger

class HttpRequestParser {
    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.simpleName)
    }

    fun parse(socket: Socket): HttpRequest {
        val bufferedInput = socket.getInputStream().bufferedReader()
        val parts = bufferedInput.readLine()?.split(" ") ?: emptyList()
        val method = HttpMethod.fromString(parts[0]) ?: throw MethodNotAllowedException(parts[0])
        val path = parts[1]
        val filePath = "www$path"
        val file = File(filePath)
        if (!file.exists() && !file.isFile) {
            throw FileNotFoundException(file.name)
        }
        val version = HttpVersion.fromString(parts[2]) ?: throw VersionNotSupportedException(parts[2])
        logger.info("Request received: $method $path $version")

        val headers = mutableMapOf<String, String>()
        while (true) {
            val line = bufferedInput.readLine() ?: break
            if (line.isBlank()) break
            val idx = line.indexOf(":")
            if (idx != -1) {
                val headerName = line.substring(0, idx).trim()
                val headerValue = line.substring(idx + 1).trim()
                headers[headerName] = headerValue
            }
        }

        return HttpRequest(method, file, version, headers, null)
    }
}