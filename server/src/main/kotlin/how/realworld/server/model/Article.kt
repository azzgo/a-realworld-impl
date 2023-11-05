package how.realworld.server.model

import how.realworld.server.repository.mapper.ArticleMapper
import how.realworld.server.repository.mapper.TagMapper
import java.time.Instant

typealias ArticleId = String

class Article(
    var slug: ArticleId? = null,
    val title: String,
    val description: String,
    val body: String,
    val author: Author,
    val tagList: List<Tag> = emptyList(),
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val favoritesCount: Int = 0,
    val favorited: Boolean = false,
)

fun Article.toMapper(): ArticleMapper {
    return ArticleMapper(
        id = slug,
        title = title,
        description = description,
        body = body,
        tagList = tagList.map { TagMapper(name = it.name) },
        createdAt = createdAt,
        updatedAt = updatedAt,
        // favoritesCount = favoritesCount,
        // favorited = favorited,
        authorId = author.userId,
    )
}