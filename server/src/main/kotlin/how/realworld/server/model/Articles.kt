package how.realworld.server.model

interface Articles {
    fun create(userId: UserId, title: String, description: String, body: String, tagList: List<String> = emptyList()): Article
    fun update(slug: ArticleId, userId: UserId, title: String, description: String, body: String, tagList: List<String> = emptyList()): Article
}