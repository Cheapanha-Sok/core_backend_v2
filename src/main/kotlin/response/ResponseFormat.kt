package response

import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus

interface ResponseFormat {
    companion object {
        private val DefaultHttpStatus: HttpStatus = HttpStatus.OK
    }

    fun respondCustomStatus(
        data: Any, status: HttpStatus? = DefaultHttpStatus, message: String? = null
    ): ResponseDTO

    fun respondDynamic(
        data: Any, status: HttpStatus? = DefaultHttpStatus, message: String? = null, total: Long
    ): ResponseDTO

    fun respondID(data: Any, status: HttpStatus? = DefaultHttpStatus, message: String? = null): ResponseDTO
    fun respondList(
        data: List<Any>, status: HttpStatus? = DefaultHttpStatus, message: String? = null
    ): ResponseDTO

    fun respondObj(data: Any, status: HttpStatus? = DefaultHttpStatus, message: String? = null): ResponseDTO
    fun <T : Any> responsePage(
        data: Page<T>?, status: HttpStatus? = DefaultHttpStatus, message: String? = null
    ): ResponseDTO
}