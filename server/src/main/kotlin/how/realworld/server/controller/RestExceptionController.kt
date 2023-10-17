package how.realworld.server.controller

import how.realworld.server.controller.dto.ErrorResponseDTO
import how.realworld.server.controller.exception.BusinessException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionController {
    @ExceptionHandler(BusinessException::class)
    fun  handleBusinessException(ex: BusinessException): ResponseEntity<ErrorResponseDTO> {
        return ResponseEntity.status(ex.statusCode).body(ErrorResponseDTO(
                errors = ex.errors
        ))
    }
}