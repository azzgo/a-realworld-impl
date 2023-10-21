package how.realworld.server.controller.dto

data class PageArticlesResponseDto(
    val articles: List<ArticleResponseDtoField>,
    val articlesCount: Int
)