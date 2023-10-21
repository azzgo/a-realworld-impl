package how.realworld.server.controller

import how.realworld.server.controller.dto.*
import how.realworld.server.controller.exception.ARTICLE_NOT_EXIST
import how.realworld.server.model.ArticleId
import how.realworld.server.model.Articles
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/articles")
class ArticleController(
        val articles: Articles,
) {

    @GetMapping
    fun listArticles(@Param("limit") limit: Int,@Param("offset") offset: Int): ResponseEntity<PageArticlesResponseDto> {
        val articles = articles.list(offset, limit, "john doe")
        return ResponseEntity.ok().body(PageArticlesResponseDto(
                articles = articles.content.map { ArticleResponseDtoField.from(it) },
                articlesCount = articles.totalElements.toInt()
        ))
    }

    @PostMapping
    fun createArticle(@RequestBody newArticleDto: NewArticleDto): ResponseEntity<ArticleResponseDto> {
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        val article = articles.create(userId = userId,
                title = newArticleDto.article.title,
                description = newArticleDto.article.description,
                body = newArticleDto.article.body,
                tagList = newArticleDto.article.tagList
        )
        return ResponseEntity.status(201).body(ArticleResponseDto(
                article = ArticleResponseDtoField.from(article)
        ))
    }

    @PutMapping("/{slug}")
    fun updateArticle(@PathVariable("slug") slug: ArticleId, @RequestBody newArticleDto: NewArticleDto): ResponseEntity<ArticleResponseDto> {
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        val article = articles.update(
                slug = slug,
                userId = userId,
                title = newArticleDto.article.title,
                description = newArticleDto.article.description,
                body = newArticleDto.article.body,
                tagList = newArticleDto.article.tagList
        )
        return ResponseEntity.ok(ArticleResponseDto(
                article = ArticleResponseDtoField.from(article)
        ))
    }

    @GetMapping("{slug}")
    fun getArticle(@PathVariable("slug") slug: ArticleId): ResponseEntity<ArticleResponseDto> {
        val article = articles.get(slug) ?: throw ARTICLE_NOT_EXIST
        return ResponseEntity.ok(ArticleResponseDto(
                article = ArticleResponseDtoField.from(article)
        ))
    }
}