package how.realworld.server.repository.mapper

import how.realworld.server.model.User
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

fun UserMapper.toModel(): User {
    return User(
        userId = id,
        username = username,
        email = email,
        bio = bio,
        image = image,
        password = password
    )
}