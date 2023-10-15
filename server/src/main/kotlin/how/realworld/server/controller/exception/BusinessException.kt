package how.realworld.server.controller.exception

class BusinessException (
    val statusCode: Int = 422,
    val errors: Map<String, List<String>>
): RuntimeException(
)
