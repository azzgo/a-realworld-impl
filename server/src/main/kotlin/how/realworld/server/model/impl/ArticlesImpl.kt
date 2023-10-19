package how.realworld.server.model.impl

import how.realworld.server.model.*
import how.realworld.server.repository.ArticleRepository
import how.realworld.server.repository.TagRepository
import how.realworld.server.repository.mapper.ArticleMapper
import how.realworld.server.repository.saveOrUpdateAll
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ArticlesImpl(
        private val users: Users,
        private val articleRepository: ArticleRepository,
        private val tagRepository: TagRepository
) : Articles {

    @Transactional
    override fun create(
            userId: String,
            title: String,
            description: String,
            body: String,
            tagList: List<String>
    ): Article {
        val user = users.getById(userId)
        val tagMappers = tagRepository.saveOrUpdateAll(tagList)
        val articleMapper = articleRepository.save(
                ArticleMapper(
                        title = title,
                        description = description,
                        body = body,
                        tagList = tagMappers,
                        authorId = user?.userId!!,
                )
        )

        return articleMapper.toModel(user)
    }
}

private fun ArticleMapper.toModel(user: User): Article {
    return Article(
            slug = id,
            title = title,
            description = description,
            body = body,
            tagList = tagList.map { Tag(it.name) },
            createdAt = createdAt,
            updatedAt = updatedAt,
            favoritesCount = favoritesCount,
            favorited = favorited,
            author = Author(
                    userId = user.userId!!,
                    username = user.username,
                    image = user.image,
                    bio = user.bio,
                    following = false
            )
    )
}
