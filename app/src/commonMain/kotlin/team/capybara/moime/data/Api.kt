package team.capybara.moime.data

object Api {
    const val BASE_URL = "https://api.moime.app/external/"

    private const val PATH_USERS = "users"
    private const val PATH_MOIMS = "moims"
    private const val PATH_FRIENDS = "users/friends"
    private const val PATH_STAT = "stat"
    private const val PATH_NOTIFICATION = "users/notifications"
    private const val PATH_SURVEY = "survey"

    const val USERS_MY = "$PATH_USERS/my"
    fun USERS_FIND_ID(id: Long) = "$PATH_USERS/id/$id"
    fun USERS_FIND_CODE(code: String) = "$PATH_USERS/code/$code"

    const val MOIMS_UPCOMING = "$PATH_MOIMS/upcoming"
    const val MOIMS_TODAY = "$PATH_MOIMS/today"
    const val MOIMS_COMPLETE = "$PATH_MOIMS/complete"
    fun MOIMS_PHOTO(moimId: Long) = "$PATH_MOIMS/$moimId/photos"
    const val MOIMS_CALENDAR = "$PATH_MOIMS/calendar"
    const val MOIMS_DATE = "$PATH_MOIMS/date"
    fun MOIMS_WITH(targetId: Long) = "$PATH_MOIMS/shared/$targetId"
    fun MOIMS_WITH_COUNT(targetId: Long) = "$PATH_MOIMS/shared/$targetId/count"

    const val WEEKLY_SUMMARY = "$PATH_STAT/weekly/moim"

    const val NOTIFICATION_EXIST = "$PATH_NOTIFICATION/unchecked/exist"

    const val FRIENDS_FOLLOWINGS = "$PATH_FRIENDS/followings"
    const val FRIENDS_RECOMMENDED = "$PATH_FRIENDS/followers/strangers"
    const val FRIENDS_COUNT = "$PATH_FRIENDS/followings/count"
    const val FRIENDS_ADD = PATH_FRIENDS
    const val FRIENDS_BLOCK = "$PATH_FRIENDS/block"
    const val FRIENDS_UNBLOCK = "$PATH_FRIENDS/unblock"
    const val FRIENDS_BLOCKED = "$PATH_FRIENDS/blocked"
    const val FRIENDS_BLOCKED_COUNT = "$PATH_FRIENDS/blocked/count"

    const val SURVEY_FRIEND_STATS = "$PATH_SURVEY/friend-stats"
}
