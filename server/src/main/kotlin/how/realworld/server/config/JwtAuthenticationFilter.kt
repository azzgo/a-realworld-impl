package how.realworld.server.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


@Configuration
class JwtAuthenticationFilter(
        @Value("\${auth.jwt.secret}") val secret: String,
) : OncePerRequestFilter() {
    private val tokenPrefix = "Token "
    private val authHeaderKey = "Authorization"

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val header = request.getHeader(authHeaderKey)
        if (header != null && header.startsWith(tokenPrefix)) {
            val token: String = header.replace(tokenPrefix, "").trim()

            // 解析并验证JWT
            val jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build()

            // 从JWT中获取用户ID
            val decodedJWT = jwtVerifier.verify(token)

            val authentication = UsernamePasswordAuthenticationToken(decodedJWT.subject, null, null)
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response)
    }
}