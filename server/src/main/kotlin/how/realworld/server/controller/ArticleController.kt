package how.realworld.server.controller

import how.realworld.server.controller.dto.ArticleResponseDto
import how.realworld.server.controller.dto.ArticleResponseDtoField
import how.realworld.server.controller.dto.NewArticleDto
import how.realworld.server.controller.dto.from
import how.realworld.server.controller.exception.ARTICLE_NOT_EXIST
import how.realworld.server.model.ArticleId
import how.realworld.server.model.Articles
import how.realworld.server.model.Users
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