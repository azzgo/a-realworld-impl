package how.realworld.server.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


class SharedContainer: PostgreSQLContainer<SharedContainer>("postgres:16.0") {
    override fun start() {
        super.start()
        System.setProperty("DB_URL", instance.getJdbcUrl());
        System.setProperty("DB_USERNAME", instance.getUsername());
        System.setProperty("DB_PASSWORD", instance.getPassword());
    }

    override fun stop() {
        // do nothing
    }

    companion object {
        private val instance: SharedContainer = SharedContainer();
        fun getInstance(): SharedContainer {
            return instance
        }
    }
}

@SpringBootTest
@AutoConfigureTestEntityManager
@ActiveProfiles("test")
@Testcontainers
open class BaseRepositoryTest {
    companion object {
        @JvmStatic
        @Container
        var postgreSQLContainer: PostgreSQLContainer<*> = SharedContainer.getInstance()

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }

}