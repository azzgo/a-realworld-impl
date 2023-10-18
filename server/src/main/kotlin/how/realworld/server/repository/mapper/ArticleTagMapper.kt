package how.realworld.server.repository.mapper

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Table(name = "t_article_tag")
@Entity
open class ArticleTagMapper (
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "idGenerator")
    open var id: String? = null,

    @ManyToOne(targetEntity = ArticleMapper::class)
    open var article: ArticleMapper,

    @ManyToOne(targetEntity = TagMapper::class)
    open var tag: TagMapper
)