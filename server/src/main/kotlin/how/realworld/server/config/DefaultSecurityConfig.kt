package how.realworld.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .authorizeHttpRequests { authorize ->
                    run {
                        authorize.requestMatchers("/user/login").permitAll()
                        authorize.anyRequest().authenticated()
                    }
                }
                .formLogin { formLogin -> formLogin.disable() }
                .httpBasic { httpBasic -> httpBasic.disable() }
                .csrf { csrf -> csrf.disable() }
                .cors { cors -> cors.disable() }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
