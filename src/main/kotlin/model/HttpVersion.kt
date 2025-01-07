package com.nikitar.model

enum class HttpVersion(val versionString: String) {
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1"),
    HTTP_2_0("HTTP/2.0");

    companion object {
        fun fromString(version: String): HttpVersion? {
            return values().find { it.versionString.equals(version, ignoreCase = true) }
        }

        fun allVersions(): List<String> {
            return values().map { it.versionString }
        }
    }
}