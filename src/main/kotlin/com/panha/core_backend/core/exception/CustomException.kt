package com.panha.core_backend.core.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


class BadCredentialsExceptionCustom(message: String, e: Exception? = null) : RuntimeException(message, e)

class BadRequestExceptionCustom(message: String, e: Exception? = null) : RuntimeException(message, e)

class NotFoundExceptionCustom(message: String, e: Exception? = null) : RuntimeException(message, e)

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
class NotAcceptableException(message: String , e: Exception? = null) : RuntimeException(message, e)