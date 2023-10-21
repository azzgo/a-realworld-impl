package how.realworld.server.model

import org.springframework.data.domain.Page

interface Articles {
    fun create(userId: UserId, title: String, description: String, body: String, tagList: List<String> = emptyList()): Article
    fun update(slug: ArticleId, userId: UserId, title: String, description: String, body: String, tagList: List<String> = emptyList()): Article
    fun get(slug: ArticleId): Article?
    fun list(page: Int, size: Int, author: String?): Page<Article>
}