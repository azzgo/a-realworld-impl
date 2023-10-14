package how.realworld.server.controller

import how.realworld.server.dto.*
import how.realworld.server.model.Users
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class AuthController(
        private val users: Users
) {
    @PostMapping("/login")
    fun login(@RequestBody userLoginDto: UserLoginDto): ResponseEntity<*> {
        val user = users.getByEmail(userLoginDto.user.email)
        if (user == null || !user.matched(userLoginDto.user.password)) {
            return ResponseEntity.status(403).body(ErrorResponseDTO(
                    errors = mapOf(Pair("email or password", listOf("is invalid")))
            ))
        }
        return ResponseEntity.status(201).body(UserLoginResponseDto(
                user = UserLoginResponseDto_User.fromUser(user, users.generateTokenForUser(user))
        ))
    }
}
