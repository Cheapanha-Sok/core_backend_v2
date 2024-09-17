package response

import java.time.LocalDateTime

data class ResponseDTO(
    var code: Int? = 0,
    var message: String? = null,
    var data: Any? = null,
    var dataList: Any? = null,
    var error: Any? = null,
    var timestamp: LocalDateTime? = LocalDateTime.now(),
    var total: Long? = 0L,
)