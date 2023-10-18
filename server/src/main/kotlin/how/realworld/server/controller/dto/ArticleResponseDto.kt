package how.realworld.server.controller.dto

import how.realworld.server.model.Article
import how.realworld.server.model.Author

data class ArticleResponseDto(val article: ArticleResponseDtoField)

data class ArticleResponseDtoField(
        val slug: String,
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String>,
        val createdAt: String,
        val updatedAt: String,
        val favorited: Boolean,
        val favoritesCount: Int,
        val author: AuthorDto
) {
    companion object
}

fun ArticleResponseDtoField.Companion.from(article: Article): ArticleResponseDtoField {
    return ArticleResponseDtoField(
            slug = article.slug!!,
            title = article.title,
            description = article.description,
            body = article.body,
            tagList = article.tagList,
            createdAt = article.createdAt.toString(),
            updatedAt = article.updatedAt.toString(),
            favorited = article.favorited,
            favoritesCount = article.favoritesCount,
            author = AuthorDto.from(article.author)
    )
}

private fun AuthorDto.Companion.from(author: Author): AuthorDto {
    return AuthorDto(
            username = author.username,
            bio = author.bio,
            image = author.image,
            following = author.following,
    )
}
