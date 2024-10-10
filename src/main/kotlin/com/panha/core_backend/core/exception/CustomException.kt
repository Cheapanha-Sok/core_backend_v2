package com.panha.core_backend.core.exception

class BadCredentialsExceptionCustom(message: String, e: Exception? = null) : RuntimeException(message, e)

class BadRequestExceptionCustom(message: String, e: Exception? = null) : RuntimeException(message, e)

class NotFoundExceptionCustom(message: String, e: Exception? = null) : RuntimeException(message, e)
