package how.realworld.server.model

import java.time.Instant

val articleLoremIpsum = createArticle(
    slug = "lorem-ipsum",
    title = "How to train your dragon",
    description = "This is the description of lorem ipsum",
    body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vestibulum diam nisi, nec auctor felis vulputate id. Phasellus euismod neque nec nibh molestie, non bibendum odio vulputate.",
    tagList = listOf("lorem", "ipsum", "dolor"),
    createdAt = Instant.parse("2022-01-01T12:00:00Z"),
    updatedAt = Instant.parse("2022-01-01T12:00:00Z"),
    favorited = false,
    favoritesCount = 0,
    author = createAuthor(
        username = "john doe",
        bio = "Lorem ipsum dolor sit amet.",
        image = "https://example.com/profile/john_doe.jpg",
        following = false
    )
)
val articleSitAmet = createArticle(
    slug = "sit-amet",
    title = "Sit Amet",
    description = "Sit amet consectetur adipiscing elit.",
    body = "Sit amet consectetur adipiscing elit. Fusce blandit, lectus a finibus convallis, tellus enim aliquam ligula, vitae feugiat lectus enim vitae urna.",
    tagList = listOf("sit", "amet", "consectetur"),
    createdAt = Instant.parse("2022-01-02T12:00:00Z"),
    updatedAt = Instant.parse("2022-01-02T12:00:00Z"),
    favorited = true,
    favoritesCount = 10,
    author = createAuthor(
        userId = "jane-smith",
        username = "jane smith",
        bio = "Sit amet consectetur adipiscing elit.",
        image = "https://example.com/profile/jane_smith.jpg",
        following = true
    )
)
val articleAdipiscingElit = createArticle(
    slug = "adipiscing-elit",
    title = "Adipiscing Elit",
    description = "Adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    body = "Adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    tagList = listOf("adipiscing", "elit", "tempor"),
    createdAt = Instant.parse("2022-01-03T12:00:00Z"),
    updatedAt = Instant.parse("2022-01-03T12:00:00Z"),
    favorited = false,
    favoritesCount = 0,
    author = createAuthor(
        userId = "alice-johnson",
        username = "alice johnson",
        bio = "Adipiscing elit sed do eiusmod tempor incididunt.",
        image = "https://example.com/profile/alice_johnson.jpg",
        following = false
    )
)
val articleConsecteturAdipiscing = createArticle(
    slug = "consectetur-adipiscing",
    title = "Consectetur Adipiscing",
    description = "Consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    body = "Consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    tagList = listOf("consectetur", "adipiscing", "tempor"),
    createdAt = Instant.parse("2022-01-04T12:00:00Z"),
    updatedAt = Instant.parse("2022-01-04T12:00:00Z"),
    favorited = true,
    favoritesCount = 15,
    author = createAuthor(
        userId = "mark-wilson",
        username = "mark wilson",
        bio = "Consectetur adipiscing elit, sed do eiusmod tempor incididunt.",
        image = "https://example.com/profile/mark_wilson.jpg",
        following = true,
    )
)
val articleLorem1 = createArticle(
    slug = "lorem-1",
    title = "Lorem Ipsum 1",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vestibulum diam nisi, nec auctor felis vulputate id. Phasellus euismod neque nec nibh molestie, non bibendum odio vulputate.",
    tagList = listOf("lorem", "ipsum", "dolor"),
    createdAt = Instant.parse("2022-01-05T12:00:00Z"),
    updatedAt = Instant.parse("2022-01-05T12:00:00Z"),
    favorited = false,
    favoritesCount = 0,
    author = createAuthor(
        userId = "mark-wilson",
        username = "mark wilson",
        bio = "Consectetur adipiscing elit, sed do eiusmod tempor incididunt.",
        image = "https://example.com/profile/mark_wilson.jpg",
        following = true
    )
)
val articleLorem2 = createArticle(
    slug = "lorem-2",
    title = "Lorem Ipsum 2",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vestibulum diam nisi, nec auctor felis vulputate id. Phasellus euismod neque nec nibh molestie, non bibendum odio vulputate.",
    tagList = listOf("lorem", "ipsum", "dolor"),
    createdAt = Instant.parse("2022-01-06T12:00:00Z"),
    updatedAt = Instant.parse("2022-01-06T12:00:00Z"),
    favorited = true,
    favoritesCount = 10,
    author = createAuthor(
        userId = "john-doe",
        username = "john doe",
        bio = "Lorem ipsum dolor sit amet.",
        image = "https://example.com/profile/john_doe.jpg",
        following = false
    )
)
val articleArtOfCooking = createArticle(
    slug = "the-art-of-cooking",
    title = "The Art of Cooking",
    description = "Discover the secrets of culinary excellence.",
    body = "In this article, we will delve into the art of cooking and explore various techniques to create mouthwatering dishes.",
    tagList = listOf("cooking", "food", "recipes"),
    createdAt = Instant.parse("2022-08-15T10:30:00Z"),
    updatedAt = Instant.parse("2022-08-15T15:45:00Z"),
    favorited = false,
    favoritesCount = 10,
    author = createAuthor(
        userId = "john-doe",
        username = "john doe",
        bio = "Lorem ipsum dolor sit amet.",
        image = "https://example.com/profile/john_doe.jpg",
        following = false
    )
)
val articleTheMagicOfMusic = createArticle(
    slug = "the-magic-of-music",
    title = "The Magic of Music",
    description = "Unleash your emotions through the power of melodies.",
    body = "Join us on a journey through the enchanting world of music, where we explore its profound impact on our lives and delve into various genres and artists.",
    tagList = listOf("music", "art", "emotions"),
    createdAt = Instant.parse("2022-08-07T14:50:00Z"),
    updatedAt = Instant.parse("2022-08-10T11:25:00Z"),
    favorited = true,
    favoritesCount = 18,
    author = createAuthor(
        userId = "jane-smith",
        username = "jane smith",
        bio = "Sit amet consectetur adipiscing elit.",
        image = "https://example.com/profile/jane_smith.jpg",
        following = true
    )
)
val articleExploringTheGreatOutDoors =
    createArticle(
        slug = "exploring-the-great-outdoors",
        title = "Exploring the Great Outdoors",
        description = "Embark on breathtaking adventures in nature.",
        body = "Join us as we explore the wonders of the great outdoors and share thrilling stories of adrenaline-pumping escapades.",
        tagList = listOf("adventure", "nature", "travel"),
        createdAt = Instant.parse("2022-08-12T09:15:00Z"),
        updatedAt = Instant.parse("2022-08-14T18:20:00Z"),
        favorited = true,
        favoritesCount = 25,
        author = createAuthor(
            userId = "adventureseeker",
            username = "adventureseeker",
            bio = "Nature lover and adrenaline junkie.",
            image = "https://example.com/images/adventureseeker.jpg",
            following = true
        )
    )
val articleThePowerOfMindfulness = createArticle(
    slug = "the-power-of-mindfulness",
    title = "The Power of Mindfulness",
    description = "Discover inner peace and cultivate mindfulness in your life.",
    body = "In this article, we explore the practice of mindfulness and its transformative effects on our mental well-being. Learn techniques to incorporate mindfulness into your daily routine.",
    tagList = listOf("mindfulness", "meditation", "selfcare"),
    createdAt = Instant.parse("2022-08-20T09:10:00Z"),
    updatedAt = Instant.parse("2022-08-22T16:35:00Z"),
    favorited = false,
    favoritesCount = 8,
    author = createAuthor(
        userId = "alice-johnson",
        username = "alice johnson",
        bio = "Adipiscing elit sed do eiusmod tempor incididunt.",
        image = "https://example.com/profile/alice_johnson.jpg",
        following = false
    )
)