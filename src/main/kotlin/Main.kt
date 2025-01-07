package com.nikitar

import com.nikitar.client.Worker
import com.nikitar.command.CommandReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.ServerSocket

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
                    Worker.handle(clientSocket)
                }
            }
        }
    }
}