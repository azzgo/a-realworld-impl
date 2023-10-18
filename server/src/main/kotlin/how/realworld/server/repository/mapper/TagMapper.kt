package how.realworld.server.repository.mapper

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator

typealias TagId = String

@Table(name = "t_tags")
@Entity
open class TagMapper (
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue
    open var id: TagId? = null,
    @Column(unique = true)
    open var name: String
)