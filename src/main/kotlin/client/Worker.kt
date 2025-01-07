package com.nikitar.client

import com.nikitar.exception.FileNotFoundException
import com.nikitar.exception.MethodNotAllowedException
import com.nikitar.model.HttpMethod
import com.nikitar.parser.HttpRequestParser
import java.io.File
import java.net.Socket
import java.util.logging.Level
import java.util.logging.Logger

class Worker {
    companion object {
        private val logger: Logger = Logger.getLogger(Worker::class.java.name)

        fun handle(clientSocket: Socket) {
            val output = clientSocket.getOutputStream().bufferedWriter()
            try {
                val parser = HttpRequestParser()
                val request = parser.parse(clientSocket)
                val mimeType = when (request.file.extension) {
                    "html" -> "text/html"
                    "css" -> "text/css"
                    "js" -> "application/javascript"
                    "png" -> "image/png"
                    "jpg", "jpeg" -> "image/jpeg"
                    else -> "application/octet-stream"
                }

                val fileBytes = request.file.readBytes()

                output.write("HTTP/1.1 200 OK\r\n")
                output.write("Content-Type: $mimeType\r\n")
                output.write("Content-Length: ${fileBytes.size}\r\n")
                output.write("\r\n")
                output.flush()

                clientSocket.getOutputStream().write(fileBytes)
                clientSocket.getOutputStream().flush()
            } catch (e: MethodNotAllowedException) {
                logger.log(Level.SEVERE, e.message)
                output.write("HTTP/1.1 405 Method Not Allowed\r\n")
                output.write("Allow: ${HttpMethod.entries}\r\n")
                output.write("Content-Length: 0\r\n")
                output.write("\r\n")
            } catch (e: FileNotFoundException) {
                logger.log(Level.SEVERE, e.message)
                output.write("HTTP/1.1 404 Not Found\r\n")
                output.write("Content-Type: text/plain\r\n")
                output.write("\r\n")
                output.write("File not found!")
            } finally {
                output.flush()
                clientSocket.close()
            }
        }
    }
}