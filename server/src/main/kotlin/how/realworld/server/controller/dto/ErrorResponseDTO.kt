package how.realworld.server.controller.dto

data class ErrorResponseDTO (
    val errors: Map<String, List<String>>
)
