package how.realworld.server.model

import java.time.Instant

typealias ArticleId = String

class Article(
        var slug: ArticleId? = null,
        val title: String,
        val description: String,
        val body: String,
        val author: Author,
        val tagList: List<String> = emptyList(),
        val createdAt: Instant = Instant.now(),
        val updatedAt: Instant = Instant.now(),
        val favoritesCount: Int = 0,
        val favorited: Boolean = false,
)
