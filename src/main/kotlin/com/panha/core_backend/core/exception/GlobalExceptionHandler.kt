package com.panha.core_backend.core.exception

import com.panha.core_backend.response.ResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundExceptionCustom::class)
    fun handleNotFoundHandler(ex: NotFoundExceptionCustom): ResponseDTO {
        val responseDTO = ResponseDTO()
        responseDTO.message = ex.message
        responseDTO.code = HttpStatus.NOT_FOUND.value()
        responseDTO.error = HttpStatus.NOT_FOUND.name
        return responseDTO
    }
}