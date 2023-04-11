package twitter

import javax.naming.AuthenticationException

class TwitterService(
    //val feedService:FeedService
) {

    private var count = 0
    val users = mutableMapOf<Int, TwitterUserImpl>()
    private val userToTweet = mutableMapOf<Int, MutableSet<Int>>()
    val tweets = mutableMapOf<Int, Tweet>()

    fun addTweet(tweetRequest: TweetRequest, userId: Int): Tweet {
        if (!validateUser(userId)) {
            throw IllegalArgumentException("user does not exist")
        }
        val tweet = Tweet(
            postedByUserId = userId,
            id = getTweetId(),
            text = tweetRequest.text,
            attachments = tweetRequest.attachments,
            metaData = TweetMetaData(info = "new tweet")
        )
        tweets.put(tweet.id, tweet)
        if (!userToTweet.containsKey(userId)) userToTweet[userId] = mutableSetOf<Int>()
        userToTweet[userId]!!.add(tweet.id)
        return tweet
    }

    fun deleteTweet(tweetId: Int, userId: Int) {
        if (!validateUser(userId)) {
            throw IllegalArgumentException("user does not exist")
        }

        val tweet = tweets[tweetId] ?: throw IllegalArgumentException("Entity not found")
        if (tweet.postedByUserId != userId) throw AuthenticationException("user not legal")

        userToTweet.remove(tweetId)
        tweets.remove(tweetId)
    }

    fun fetchFeed(userId: Int, page: Int?, pageSize: Int?): PagedFeed {
        TODO()
    }

    fun follow(requestedById: Int, followeeId: Int) {
        if (!validateUser(requestedById) || !validateUser(followeeId)) {
            throw IllegalArgumentException("user does not exist")
        }
        val followee = users[followeeId]
        followee!!.addFollower(requestedById)
    }

    private fun validateUser(userId: Int): Boolean {
        return users.containsKey(userId)
    }

    private fun getTweetId(): Int {
        return count++
    }
}