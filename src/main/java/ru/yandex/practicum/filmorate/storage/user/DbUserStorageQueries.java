package ru.yandex.practicum.filmorate.storage.user;

public final class DbUserStorageQueries {
    public static final String FIND_BY_ID_QUERY = """
            SELECT id, login, email, name, birthday
            FROM users
            WHERE id = ?;""";

    public static final String FIND_ALL_QUERY = """
            SELECT id, login, email, name, birthday
            FROM users;""";

    public static final String DELETE_BY_ID_QUERY = """
            DELETE
            FROM users
            WHERE id = ?;""";

    public static final String DELETE_ALL_QUERY = """
            DELETE
            FROM users;""";

    public static final String INSERT_QUERY = """
            INSERT INTO users (login, email, name, birthday)
            VALUES (?, ?, ?, ?);""";

    public static final String UPDATE_BY_ID_QUERY = """
            UPDATE users
            SET login = ?,
                email = ?,
                name = ?,
                birthday = ?
            WHERE id = ?;""";

    private DbUserStorageQueries() {
    }
}
