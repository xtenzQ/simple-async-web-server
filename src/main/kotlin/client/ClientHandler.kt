package com.nikitar.client

import com.nikitar.request.IncomingRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File
import java.lang.Exception
import java.net.Socket
import java.util.logging.Logger

class ClientHandler {
    companion object {
        private val logger: Logger = Logger.getLogger(ClientHandler::class.java.name)

        fun handle(clientSocket: Socket) {
            try {
                val input = clientSocket.getInputStream().bufferedReader()
                val output = clientSocket.getOutputStream().bufferedWriter()

                val requestLine = input.readLine() ?: ""
                logger.info { requestLine }

                val incomingRequest = IncomingRequest.fromString(requestLine)

                val headers = mutableMapOf<String, String>()
                while (true) {
                    val line = input.readLine() ?: break
                    if (line.isBlank()) break
                    val idx = line.indexOf(":")
                    if (idx != -1) {
                        val headerName = line.substring(0, idx).trim()
                        val headerValue = line.substring(idx + 1).trim()
                        headers[headerName] = headerValue
                    }
                }

                logger.info { headers.toString() }

                val filePath = "www${incomingRequest.path}"
                val file = File(filePath)
                if (file.exists() && file.isFile) {
                    val mimeType = when (file.extension) {
                        "html" -> "text/html"
                        "css" -> "text/css"
                        "js" -> "application/javascript"
                        "png" -> "image/png"
                        "jpg", "jpeg" -> "image/jpeg"
                        else -> "application/octet-stream"
                    }

                    val fileBytes = file.readBytes()

                    output.write("HTTP/1.1 200 OK\r\n")
                    output.write("Content-Type: $mimeType\r\n")
                    output.write("Content-Length: ${fileBytes.size}\r\n")
                    output.write("\r\n")
                    output.flush()

                    clientSocket.getOutputStream().write(fileBytes)
                    clientSocket.getOutputStream().flush()
                } else {
                    output.write("HTTP/1.1 404 Not Found\r\n")
                    output.write("Content-Type: text/plain\r\n")
                    output.write("\r\n")
                    output.write("File not found!")
                }

                clientSocket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}