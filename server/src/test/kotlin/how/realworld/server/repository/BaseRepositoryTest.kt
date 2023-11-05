package how.realworld.server.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest
@AutoConfigureTestEntityManager
@ActiveProfiles("test")
@Testcontainers
open class BaseRepositoryTest {

    companion object {
        @Container
        var postgreSQLContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:16.0")
            .withDatabaseName("conduit_test")
            .withUsername("sa")
            .withPassword("password")

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }

}