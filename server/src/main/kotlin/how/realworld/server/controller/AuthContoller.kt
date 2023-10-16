package how.realworld.server.controller

import how.realworld.server.controller.dto.*
import how.realworld.server.controller.exception.BusinessException
import how.realworld.server.model.Users
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class AuthController(
    private val users: Users,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping()
    fun createUser(@RequestBody userRegisterDto: UserRegisterDto): ResponseEntity<UserAuthenticationResponseDto> {
        verityUserExist(userRegisterDto)
        val user =
            users.createUser(userRegisterDto.user.email, userRegisterDto.user.username, userRegisterDto.user.password)

        return ResponseEntity.status(201).body(
            UserAuthenticationResponseDto(
                user = UserAuthenticationResponseDtoUserField.fromUser(user, users.generateTokenForUser(user))
            )
        )
    }

    private fun verityUserExist(userRegisterDto: UserRegisterDto) {
        val checkUserExist = users.checkUserExist(userRegisterDto.user.email, userRegisterDto.user.username)
        val errors = mutableMapOf<String, List<String>>()
        if (checkUserExist.email) {
            errors.put("email", listOf("has already been taken"))
        }
        if (checkUserExist.username) {
            errors.put("username", listOf("has already been taken"))
        }
        if (!errors.isEmpty()) {
            throw BusinessException(422, errors)
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody userLoginDto: UserLoginDto): ResponseEntity<UserAuthenticationResponseDto> {
        val user = users.getByEmail(userLoginDto.user.email)
        if (user == null || !passwordEncoder.matches(userLoginDto.user.password, user.password)) {
            throw BusinessException(403, mapOf(Pair("email or password", listOf("is invalid"))))
        }
        return ResponseEntity.status(201).body(
            UserAuthenticationResponseDto(
                user = UserAuthenticationResponseDtoUserField.fromUser(user, users.generateTokenForUser(user))
            )
        )
    }
}
