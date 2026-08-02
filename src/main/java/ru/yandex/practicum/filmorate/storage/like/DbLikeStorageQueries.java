package ru.yandex.practicum.filmorate.storage.like;

public final class DbLikeStorageQueries {
    public static final String FIND_BY_ID_QUERY = """
            SELECT film_id, user_id
            FROM film_likes
            WHERE film_id = ?;""";

    public static final String FIND_ALL_QUERY = """
            SELECT film_id, user_id
            FROM film_likes;""";

    public static final String INSERT_QUERY = """
            INSERT INTO film_likes (film_id, user_id)
            VALUES (?, ?);""";

    public static final String DELETE_QUERY = """
            DELETE
            FROM film_likes
            user_id = ? AND film_id = ?;""";


    private DbLikeStorageQueries() {
    }
}
