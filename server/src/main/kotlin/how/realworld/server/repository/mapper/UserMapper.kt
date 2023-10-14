package how.realworld.server.repository.mapper

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Table(name = "t_users")
@Entity
open class UserMapper(
        @Id
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        val id: String,
        val username: String,
        val password: String,
        val email: String,
        val bio: String,
        val image: String,
)