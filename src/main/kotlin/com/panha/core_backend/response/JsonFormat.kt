package com.panha.core_backend.response

import com.panha.core_backend.utilities.UtilService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class JsonFormat(private val utilService: UtilService) : ResponseFormat {

    override fun respondCustomStatus(data: Any, status: HttpStatus?, message: String?): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data)
    }

    override fun respondDynamic(data: Any, status: HttpStatus?, message: String?, total: Long): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data)
    }

    override fun respondID(data: Any, status: HttpStatus?, message: String?): ResponseDTO {
        return ResponseDTO(
            code = status?.value(),
            message = message,
            data = mapOf("id" to utilService.getValueFromField(data, "id"))
        )
    }

    override fun respondList(data: List<Any>, status: HttpStatus?, message: String?): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data, total = data.count().toLong())
    }

    override fun respondObj(data: Any, status: HttpStatus?, message: String?): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data)
    }

    override fun <T : Any> responsePage(data: Page<T>?, status: HttpStatus?, message: String?): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data, total = data?.totalPages?.toLong())
    }
}