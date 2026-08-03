package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmLikeRowMapper implements RowMapper<FilmLike> {
    @Override
    public FilmLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FilmLike(rs.getInt("film_id"),
                rs.getInt("user_id"));
    }
}
