package how.realworld.server.repository.mapper

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

typealias TagId = String

@Table(name = "t_tags", uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
@Entity
open class TagMapper (
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "idGenerator")
    open var id: TagId? = null,
    @Column(unique = true)
    open var name: String
)