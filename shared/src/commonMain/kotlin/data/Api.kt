package data

object Api {
    const val BASE_URL = "https://api.moime.app/external/"

    private const val PATH_USERS = "users"
    private const val PATH_MOIMS = "moims"
    private const val PATH_FRIENDS = "users/friends"

    const val USERS_MY = "$PATH_USERS/my"
    const val USERS_FIND= PATH_USERS

    const val MOIMS_UPCOMING = "$PATH_MOIMS/upcoming"
    const val MOIMS_TODAY = "$PATH_MOIMS/today"
    const val MOIMS_COMPLETE = "$PATH_MOIMS/complete"

    const val FRIENDS_FOLLOWINGS = "$PATH_FRIENDS/followings"
    const val FRIENDS_RECOMMENDED = "$PATH_FRIENDS/followers/strangers"
    const val FRIENDS_COUNT = "$PATH_FRIENDS/followings/count"
    const val FRIENDS_ADD = PATH_FRIENDS
}
