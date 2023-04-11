package twitter

class TwitterUserImpl(
    val id: Int,
    val name: String,
    val metaData: UserMetaData?
) {

    // list of followers
    val followers = mutableSetOf<Int>()

    fun addFollower(followerId: Int) {
        TODO()
    }

    fun removeFollower(followerId: Int) {
        TODO()
    }
}

data class UserMetaData(
    val phone: Int
)