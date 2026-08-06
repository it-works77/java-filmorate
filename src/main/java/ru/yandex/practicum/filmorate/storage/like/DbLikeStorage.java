package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Qualifier("dbLikeStorage")
public class DbLikeStorage extends BaseDbStorage<FilmLike> implements LikeStorage {

    public DbLikeStorage(JdbcTemplate jdbc, RowMapper<FilmLike> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        insertWithCompositePrimaryKey(DbLikeStorageQueries.INSERT_QUERY, filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        delete(DbLikeStorageQueries.DELETE_QUERY, filmId, userId);
    }

    @Override
    public List<Integer> getFilmLikes(Integer filmId) {
        return findMany(DbLikeStorageQueries.FIND_BY_ID_QUERY, filmId).stream()
                .map(FilmLike::getUserId)
                .toList();
    }

    @Override
    public Map<Integer, HashSet<Integer>> getAllLikes() {
        List<FilmLike> likesList = findMany(DbLikeStorageQueries.FIND_ALL_QUERY);
        return likesList.stream()
                .collect(Collectors.groupingBy(
                        FilmLike::getFilmId,
                        Collectors.mapping(
                                FilmLike::getUserId,
                                Collectors.toCollection(HashSet::new)
                        )
                ));
    }
}
