package com.nikitar.request

class IncomingRequest(val method: String, val path: String, val version: String) {
    companion object {
        fun fromString(input: String): IncomingRequest {
            val parts = input?.split(" ") ?: emptyList()
            require(parts.size == 3)
            return IncomingRequest(parts[0], parts[1], parts[2])
        }
    }
}