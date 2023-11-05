package how.realworld.server.model

import java.time.Instant

fun createUser(
    userId: String? = null,
    username: String = "jake",
    email: String = "jake@jake.jake",
    bio: String? = null,
    image: String? = null
): User {
    return User(userId = userId, username = username, email = email, bio = bio, image = image, password = "password")
}

fun createArticle(
    slug: ArticleId? = null,
    title: String = "title",
    description: String = "description",
    body: String = "body",
    tagList: List<String> = listOf(),
    createdAt: Instant = Instant.parse("2020-01-01T00:00:00Z"),
    updatedAt: Instant = Instant.parse("2020-01-01T00:00:00Z"),
    favoritesCount: Int = 0,
    favorited: Boolean = false,
    author: Author = createAuthor(),
) = Article(
    slug = slug,
    title = title,
    description = description,
    body = body,
    tagList = tagList.map { Tag(name = it) },
    favoritesCount = favoritesCount,
    favorited = favorited,
    createdAt = createdAt,
    updatedAt = updatedAt,
    author = author
)

fun createAuthor(
    userId: String = "jake_id",
    username: String = "jake",
    bio: String? = null,
    image: String? = null,
    following: Boolean = false
) =
    Author(userId, username = username, bio = bio, image = image, following = following)