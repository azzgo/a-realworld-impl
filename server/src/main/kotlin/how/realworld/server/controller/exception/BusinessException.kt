package how.realworld.server.controller.exception

class BusinessException (
    val statusCode: Int = 422,
    val errors: Map<String, List<String>>
): RuntimeException(
)

val USER_NOT_VALID = BusinessException(
    errors = mapOf(
        "user" to listOf("not valid")
    )
)