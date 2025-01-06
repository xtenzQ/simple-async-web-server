package com.nikitar

import com.nikitar.client.ClientHandler
import com.nikitar.command.CommandReader
import com.nikitar.request.IncomingRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket

fun main(args: Array<String>) {
    val config = CommandReader.parseArgs(args)

    runBlocking {
        launch(Dispatchers.IO) {
            // master process
            val serverSocket = ServerSocket(config.port)

            while (true) {
                val clientSocket = serverSocket.accept()
                // create new worker for each request
                launch(Dispatchers.IO) {
                    handleClient(clientSocket)
                }
            }
        }
    }
}

suspend fun handleClient(clientSocket: Socket) {
    ClientHandler.handle(clientSocket)
}