package response

import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class JsonFormat : ResponseFormat {

    override fun respondCustomStatus(data: Any, status: HttpStatus?, message: String?): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data)
    }

    override fun respondDynamic(data: Any, status: HttpStatus?, message: String?, total: Long): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data)
    }

    override fun respondID(data: Any, status: HttpStatus?, message: String?): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data.javaClass.getField("id"))
    }

    override fun respondList(data: List<Any>, status: HttpStatus?, message: String?): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data ,total = data.count().toLong())
    }

    override fun respondObj(data: Any, status: HttpStatus?, message: String?): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data)
    }

    override fun <T : Any> responsePage(data: Page<T>?, status: HttpStatus?, message: String?): ResponseDTO {
        return ResponseDTO(code = status?.value(), message = message, data = data , total = data?.totalPages?.toLong())
    }
}