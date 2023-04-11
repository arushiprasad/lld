import twitter.TweetRequest
import twitter.TwitterService
import twitter.TwitterUserImpl

fun main(args: Array<String>) {

    val twitter = TwitterService()

    val user1 = TwitterUserImpl(1, "arushi", null)
    val user2 = TwitterUserImpl(2, "akash", null)
    twitter.users.put(1, user1)
    twitter.users.put(2, user2)

    val tweet = twitter.addTweet(TweetRequest("hello", emptyList()), 1)
    println(twitter.tweets)
    val rmeove = twitter.deleteTweet(tweet.id, 1)
    println(twitter.tweets)
}

