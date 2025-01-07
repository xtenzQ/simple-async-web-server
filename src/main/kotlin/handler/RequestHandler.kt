package com.nikitar.handler

import com.nikitar.model.HttpRequest
import com.nikitar.model.HttpResponse

interface RequestHandler {
    fun handle(request: HttpRequest): HttpResponse
}