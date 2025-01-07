package com.nikitar.model

import java.io.File

class HttpRequest(
    val method: HttpMethod,
    val file: File,
    val version: HttpVersion,
    val headers: Map<String, String>,
    val body: String?
) {
    fun getHeader(key: String): String? {
        return headers[key]
    }
}