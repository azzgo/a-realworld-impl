package how.realworld.server.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import how.realworld.server.controller.exception.BusinessException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import java.io.IOException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
class JwtAuthenticationFilter(
    @Qualifier("handlerExceptionResolver")
    private val resolver: HandlerExceptionResolver,
    @Value("\${auth.jwt.secret}") val secret: String
) :
    OncePerRequestFilter() {
    private val tokenPrefix = "Token "
    private val authHeaderKey = "Authorization"

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = request.getHeader(authHeaderKey)
        if (header != null && header.startsWith(tokenPrefix)) {
            val token: String = header.replace(tokenPrefix, "").trim()

            val jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build()

            jwtVerifier.runCatching {
                val decodedJWT = verify(token)
                val authentication = UsernamePasswordAuthenticationToken(decodedJWT.subject, null, null)
                SecurityContextHolder.getContext().authentication = authentication
            }.getOrElse {
                if (it is JWTVerificationException) {
                    resolver.resolveException(
                        request, response, null, BusinessException(
                            401, mapOf(
                                "token" to listOf("is invalid"),
                                "message" to listOf(it.message!!)
                            )
                        )
                    )
                } else
                    throw it

            }

        }
        chain.doFilter(request, response)
    }
}
