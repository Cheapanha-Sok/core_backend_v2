package com.panha.core_backend.response

import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus

abstract class ResponseFormat {

    // Json Response
    companion object {
        private val DefaultHttpStatus: HttpStatus = HttpStatus.OK
    }

    abstract fun respondCustomStatus(
        data: Any, status: HttpStatus? = DefaultHttpStatus, message: String? = null
    ): ResponseDTO

    abstract fun responseList(data: Any, status: HttpStatus? = DefaultHttpStatus, message: String? = null): ResponseDTO
    abstract fun respondDynamic(
        data: Any,
        status: HttpStatus? = DefaultHttpStatus,
        message: String? = null,
        total: Long
    ): ResponseDTO

    abstract fun respondID(data: Any, status: HttpStatus? = DefaultHttpStatus, message: String? = null): ResponseDTO
    abstract fun respondList(
        data: List<Any>,
        status: HttpStatus? = DefaultHttpStatus,
        message: String? = null
    ): ResponseDTO

    abstract fun respondObj(data: Any, status: HttpStatus? = DefaultHttpStatus, message: String? = null): ResponseDTO
    abstract fun <T : Any> responsePage(
        data: Page<T>?,
        status: HttpStatus? = DefaultHttpStatus,
        message: String? = null
    ): ResponseDTO

    // Xml Response And More

}