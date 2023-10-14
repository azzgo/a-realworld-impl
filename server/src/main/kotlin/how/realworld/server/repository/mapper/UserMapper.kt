package how.realworld.server.repository.mapper

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Table(name = "t_users")
@Entity
open class UserMapper(
        @Id
        @GenericGenerator(name = "idGenerator", strategy = "uuid2")
        @GeneratedValue(generator = "idGenerator")
        open var id: String? = null,
        open var username: String,
        open var password: String,
        open var email: String,
        open var bio: String? = null,
        open var image: String? = null,
)