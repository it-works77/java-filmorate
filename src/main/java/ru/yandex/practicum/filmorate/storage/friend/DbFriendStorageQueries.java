package ru.yandex.practicum.filmorate.storage.friend;

public final class DbFriendStorageQueries {
    public static final String FIND_BY_ID_QUERY = """
            SELECT friend_id, is_confirmed
            FROM user_friends
            WHERE user_id = ?;""";

    public static final String FIND_ALL_QUERY = """
            SELECT friend_id, is_confirmed
            FROM user_friends;""";

    public static final String INSERT_QUERY = """
            INSERT INTO user_friends (user_id, friend_id, is_confirmed)
            VALUES (?, ?, ?);""";

    public static final String DELETE_QUERY = """
            DELETE
            FROM user_friends
            WHERE user_id = ? AND friend_id = ?;""";

    public static final String UPDATE_IS_CONFIRMED_QUERY = """
            UPDATE user_friends
            SET is_confirmed = ?
            WHERE user_id = ? AND friend_id = ?;""";

    private DbFriendStorageQueries() {
    }
}
