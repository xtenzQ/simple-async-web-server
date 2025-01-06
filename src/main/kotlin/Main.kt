package com.nikitar

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket

fun main() {
    runBlocking {
        launch(Dispatchers.IO) {
            val serverSocket = ServerSocket(8080)

            while (true) {
                val clientSocket = serverSocket.accept()
                launch(Dispatchers.IO) {
                    handleClient(clientSocket)
                }
            }
        }
    }
}

suspend fun handleClient(clientSocket: Socket) {
    try {
        val input = clientSocket.getInputStream().bufferedReader()
        val output = clientSocket.getOutputStream().bufferedWriter()

        val requestLine = input.readLine() ?: ""
        println("Request: $requestLine")

        val parts = requestLine?.split(" ") ?: emptyList()
        var path = ""
        if (parts.size == 3) {
            val method = parts[0]
            path = parts[1]
            val httpVersion = parts[2]
        }

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

        val filePath = "www$path"
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