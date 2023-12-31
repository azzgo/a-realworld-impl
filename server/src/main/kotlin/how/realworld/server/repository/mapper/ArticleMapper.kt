package how.realworld.server.repository.mapper

import how.realworld.server.model.UserId
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.Instant


@Table(name = "t_articles")
@Entity
open class ArticleMapper(
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "idGenerator")
    open var id: String? = null,
    open var title: String,
    open var description: String,
    open var body: String,
    @ManyToMany(targetEntity = TagMapper::class, fetch = FetchType.EAGER)
    @JoinTable(
        name = "t_article_tag",
        joinColumns = [JoinColumn(name = "article_id",  referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id", referencedColumnName = "id")],
        uniqueConstraints = [UniqueConstraint(columnNames = ["article_id", "tag_id"])]
    )
    open var tagList: List<TagMapper>,
    open var createdAt: Instant = Instant.now(),
    open var updatedAt: Instant = Instant.now(),
    //    open var favoritesCount: Int = 0,
    //    open var favorited: Boolean = false,
    open var authorId: UserId,
)