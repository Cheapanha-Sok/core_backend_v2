package com.panha.core_backend.core.exception

import com.panha.core_backend.response.ResponseDTO
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
class GlobalResponseException {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundExceptionCustom::class)
    fun handleNotFoundHandler(ex : NotFoundExceptionCustom) :ResponseDTO{
        val responseDTO = ResponseDTO()
        responseDTO.message = ex.message
        responseDTO.code = HttpStatus.NOT_FOUND.value()
        responseDTO.error = HttpStatus.NOT_FOUND.name
        return responseDTO
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleGenericHandler(ex: Exception): ResponseDTO {
        val responseDTO = ResponseDTO()
        responseDTO.message = ex.message
        responseDTO.code = HttpStatus.INTERNAL_SERVER_ERROR.value()
        responseDTO.error = HttpStatus.INTERNAL_SERVER_ERROR.name
        return responseDTO
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @ExceptionHandler(BadRequestExceptionCustom::class)
    fun handleBadRequestHandler(ex : BadRequestExceptionCustom) :ResponseDTO{
        val responseDTO = ResponseDTO()
        responseDTO.message = ex.message
        responseDTO.code = HttpStatus.BAD_REQUEST.value()
        responseDTO.error = HttpStatus.BAD_REQUEST.name
        return responseDTO
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsExceptionCustom::class)
    fun handleBadCredentialHandler(ex : BadCredentialsExceptionCustom) :ResponseDTO{
        val responseDTO = ResponseDTO()
        responseDTO.message = ex.message
        responseDTO.code = HttpStatus.UNAUTHORIZED.value()
        responseDTO.error = HttpStatus.UNAUTHORIZED.name
        return responseDTO
    }


    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NullPointerException::class)
    fun handleNullPointerHandler(ex : NullPointerException) :ResponseDTO{
        val responseDTO = ResponseDTO()
        responseDTO.message = "Some filed are null or incorrect!"
        responseDTO.code = HttpStatus.BAD_REQUEST.value()
        responseDTO.error = HttpStatus.BAD_REQUEST.name
        return responseDTO
    }
}