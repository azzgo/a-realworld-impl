package how.realworld.server.controller

import how.realworld.server.controller.dto.UserAuthenticationResponse
import how.realworld.server.controller.dto.UserAuthenticationResponseDto
import how.realworld.server.controller.dto.fromUser
import how.realworld.server.controller.exception.USER_NOT_VALID
import how.realworld.server.model.Users
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
        val users: Users
) {
    @GetMapping()
    fun getCurrentUser(): UserAuthenticationResponseDto {
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        val user = users.getById(userId)
                ?: throw USER_NOT_VALID
        return UserAuthenticationResponseDto(
                user = UserAuthenticationResponse.Companion.fromUser(user)
        )
    }
}