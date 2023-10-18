package how.realworld.server.model.impl

import how.realworld.server.model.Article
import how.realworld.server.model.Articles
import org.springframework.stereotype.Service

@Service
class ArticlesImpl : Articles {
    override fun create(userId: String, title: String, description: String, body: String, tagList: List<String>): Article {
        TODO("Not yet implemented")
    }
}