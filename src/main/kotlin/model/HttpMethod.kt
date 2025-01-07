package com.nikitar.model

enum class HttpMethod {
    GET, POST, PUT, DELETE, OPTIONS, HEAD;

    companion object {
        fun fromString(method: String): HttpMethod? {
            return values().find { it.name.equals(method, ignoreCase = true) }
        }
    }
}