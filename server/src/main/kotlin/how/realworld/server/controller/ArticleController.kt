package how.realworld.server.controller

import how.realworld.server.controller.dto.ArticleResponseDto
import how.realworld.server.controller.dto.ArticleResponseDtoField
import how.realworld.server.controller.dto.NewArticleDto
import how.realworld.server.controller.dto.from
import how.realworld.server.model.Articles
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/articles")
class ArticleController(
        val articles: Articles
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
}