package ru.yandex.practicum.filmorate.storage.film;

public final class DbFilmStorageQueries {
    public static final String FIND_BY_ID_QUERY = """
            SELECT name, description, release_date, duration, mpa
            FROM films
            WHERE id = ?""";

    public static final String FIND_ALL_QUERY = """
            SELECT id, name, description, release_date, duration, mpa
            FROM films;""";

    public static final String DELETE_BY_ID_QUERY = """
            DELETE
            FROM films
            WHERE id = ?""";

    public static final String INSERT_QUERY = """
            INSERT INTO films (name, description, release_date, duration, mpa)
            VALUES (?, ?, ?, ?, ?);""";

    public static final String UPDATE_BY_ID_QUERY = """
            UPDATE films
            SET name=?,
                description=?,
                release_date=?,
                duration=?,
                mpa=?
            WHERE id=?;""";


    private DbFilmStorageQueries() {
    }
}
