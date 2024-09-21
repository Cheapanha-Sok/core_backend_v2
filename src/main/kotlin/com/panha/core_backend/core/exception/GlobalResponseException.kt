package com.panha.core_backend.core.exception

import com.panha.core_backend.response.ResponseDTO
import jakarta.persistence.EntityNotFoundException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Order(Integer.MIN_VALUE)
@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<ResponseDTO> {
        val message = ex.message ?: ex.cause?.message ?: ex.stackTraceToString()
        val response = createResponse(HttpStatus.INTERNAL_SERVER_ERROR, message)
        logger.error(message)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors = ex.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" } +
                ex.bindingResult.globalErrors.map { "${it.objectName}: ${it.defaultMessage}" }

        val response = createResponse(HttpStatus.valueOf(status.value()), errors)
        logger.error(errors.toString())
        return ResponseEntity(response, headers, status)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val message = ex.cause?.message ?: "Unexpected Error: JSON parse"
        val response = createResponse(HttpStatus.valueOf(status.value()), message)
        logger.error(message)
        return ResponseEntity(response, headers, status)
    }

    override fun handleHttpMessageNotWritable(
        ex: HttpMessageNotWritableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val message = ex.cause?.message ?: "Unexpected Error: JSON parse"
        val response = createResponse(HttpStatus.valueOf(status.value()), message)
        logger.error(message)
        return ResponseEntity(response, headers, status)
    }

    @ExceptionHandler(NotFoundExceptionCustom::class)
    fun handleEntityNotFounds(ex: NotFoundExceptionCustom?, request: WebRequest): ResponseEntity<ResponseDTO> {
        val message = ex?.message ?: "Unexpected Error"
        val response = createResponse(HttpStatus.NOT_FOUND, message)
        logger.error(message)
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }


    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityAcceptable(ex: EntityNotFoundException?, request: WebRequest): ResponseEntity<ResponseDTO> {
        val message = ex?.message ?: "Unexpected Error"
        val response = createResponse(HttpStatus.NOT_ACCEPTABLE, message)
        logger.error(message)
        return ResponseEntity(response, HttpStatus.NOT_ACCEPTABLE)
    }

    private fun createResponse(status: HttpStatus, message: Any): ResponseDTO {
        return ResponseDTO(
            code = status.value(),
            message = status.reasonPhrase,
            error = message,
            data = null,
            dataList = null,
            total = null
        )
    }
}
