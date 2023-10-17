package how.realworld.server.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
class JwtAuthenticationFilter(@Value("\${auth.jwt.secret}") val secret: String) :
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

            val decodedJWT = jwtVerifier.verify(token)

            val authentication = UsernamePasswordAuthenticationToken(decodedJWT.subject, null, null)
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response)
    }
}
