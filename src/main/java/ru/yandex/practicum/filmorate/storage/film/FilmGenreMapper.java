package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmGenreMapper implements RowMapper<FilmGenre> {
    @Override
    public FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FilmGenre(rs.getString("genre"));
    }
}
