package how.realworld.server.controller.dto

data class NewArticleDto(val article: NewArticleDtoField)

data class NewArticleDtoField(
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String>
)
