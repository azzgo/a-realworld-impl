package how.realworld.server.model

interface Articles {
    fun create(userId: String, title: String, description: String, body: String, tagList: List<String> = emptyList()): Article
}