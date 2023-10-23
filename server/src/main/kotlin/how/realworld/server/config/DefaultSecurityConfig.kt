package how.realworld.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
        val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .authorizeHttpRequests { authorize ->
                    run {
                        authorize.requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        authorize.requestMatchers(HttpMethod.POST, "/users").permitAll()
                        authorize.requestMatchers(HttpMethod.GET, "/articles/**").permitAll()
                        authorize.requestMatchers(HttpMethod.GET, "/articles").permitAll()
                        authorize.anyRequest().authenticated()
                    }
                }
                .sessionManagement { sessionManagementCustomizer ->
                    sessionManagementCustomizer.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
                }
                .formLogin { formLogin -> formLogin.disable() }
                .httpBasic { httpBasic -> httpBasic.disable() }
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
                .csrf { csrf -> csrf.disable() }
                .cors { cors -> cors.disable() }
                .sessionManagement { sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
