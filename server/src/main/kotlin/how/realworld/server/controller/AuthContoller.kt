package how.realworld.server.controller

import how.realworld.server.dto.ErrorResponseDTO
import how.realworld.server.dto.UserLoginDto
import how.realworld.server.dto.UserLoginResponseDto
import how.realworld.server.dto.UserLoginResponseDtoUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class AuthController(
) {
    @PostMapping("/login")
    fun login(@RequestBody userLoginDto: UserLoginDto): ResponseEntity<*> {
        if (userLoginDto.user.email != "jake@jake.jake") {
            return ResponseEntity.status(403).body(ErrorResponseDTO(
                    errors = mapOf(Pair("email or password", listOf("is invalid")))
            ))
        }
        return ResponseEntity.status(201).body(UserLoginResponseDto(
                user = UserLoginResponseDtoUser(
                        email = "jake@jake.jake",
                        token = "jwt.token.here",
                        bio = "I work at statefarm",
                        image = "http://image.url",
                        username = "jake"
                )
        ))
    }
}
