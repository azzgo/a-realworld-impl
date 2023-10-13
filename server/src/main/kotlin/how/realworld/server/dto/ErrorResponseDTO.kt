package how.realworld.server.dto

data class ErrorResponseDTO (
    val errors: Map<String, List<String>>
)
