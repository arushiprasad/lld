package twitter

data class TweetRequest(
    val text: String,
    val attachments: List<Attachment>,
)

data class Tweet(
    val postedByUserId: Int,
    val id: Int,
    val text: String,
    val attachments: List<Attachment>,
    val metaData: TweetMetaData
)

data class Feed(
    val tweets: List<Tweet>
)

data class PagedFeed(
    val tweets: List<Tweet>,
    val pageSize: Int? = 10,
    val pageNo: Int,
    val nextPage: Int,
    val totalPages: Int
)

data class TweetMetaData(
    val info: String
)

interface Attachment {
    val source: String
}

data class Image(
    override val source: String
) : Attachment