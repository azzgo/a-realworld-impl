package how.realworld.server.model.impl

import how.realworld.server.controller.exception.ARTICLE_NOT_EXIST
import how.realworld.server.controller.exception.USER_NOT_VALID
import how.realworld.server.model.*
import how.realworld.server.repository.*
import how.realworld.server.repository.mapper.ArticleMapper
import how.realworld.server.repository.mapper.toModel
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ArticlesImpl(
    private val userRepository: UserRepository,
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
        val user = userRepository.findById(userId).getOrNull() ?: throw USER_NOT_VALID
        val tagMappers = tagRepository.saveOrUpdateAll(tagList)
        val articleMapper = articleRepository.save(
            ArticleMapper(
                title = title,
                description = description,
                body = body,
                tagList = tagMappers,
                authorId = user.id!!,
            )
        )

        return articleMapper.toModel(user.toModel())
    }

    override fun update(
        slug: ArticleId,
        userId: UserId,
        title: String,
        description: String,
        body: String,
        tagList: List<String>
    ): Article {
        val user = userRepository.findById(userId).getOrNull() ?: throw USER_NOT_VALID
        val articleMapper = articleRepository.findById(slug).getOrNull() ?: throw ARTICLE_NOT_EXIST
        val tagMappers = tagRepository.saveOrUpdateAll(tagList)
        articleMapper.title = title
        articleMapper.description = description
        articleMapper.body = body
        articleMapper.tagList = tagMappers
        val updatedArticleMapper = articleRepository.save(articleMapper)

        return updatedArticleMapper.toModel(user.toModel())
    }

    override fun get(slug: ArticleId): Article? {
        val articleMapper = articleRepository.findById(slug).getOrNull() ?: return null
        val user = userRepository.findById(articleMapper.authorId).getOrNull()?.toModel()
        return articleMapper.toModel(user!!)
    }

    override fun list(offset: Int, limit: Int, author: String?, tag: String?): Page<Article> {
        var authorId: String? = null
        if (author != null) {
            val foundUser = userRepository.findByUsername(author) ?: return Page.empty()
            authorId = foundUser.id
        }
        val result = articleRepository.queryList(
            tagName = tag,
            authorId = authorId,
            pageable = PageRequest.of(offset, limit, Sort.by("createdAt").descending())
        )
        val userMappers = userRepository.findByIds(result.toList().map { it.authorId })
        val userIdToUserMapperMap = userMappers.associateBy { it.id }
        return result.map { it.toModel(userIdToUserMapperMap[it.authorId]!!.toModel()) }
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
